package cz.nedbalek.nytimessample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import cz.nedbalek.nytimessample.R
import cz.nedbalek.nytimessample.connection.Api
import cz.nedbalek.nytimessample.connection.ArticlesResponse
import cz.nedbalek.nytimessample.ui.activity.DetailActivity
import cz.nedbalek.nytimessample.ui.adapter.ArticlesAdapter
import cz.nedbalek.nytimessample.ui.helpers.CardViewDecorator
import kotlinx.android.synthetic.main.base_content_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Fragment template for displaying [ContentType] customized screens with content.
 */
class BaseContentFragment : Fragment(), Callback<ArticlesResponse> {
    enum class ContentType {
        MAILED,
        SHARED,
        VIEWED
    }

    private val adapter by lazy {
        ArticlesAdapter(requireContext()) { DetailActivity.create(requireActivity(), it) }
    }

    lateinit var apiCall: Call<ArticlesResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiCall = when (ContentType.values()[requireArguments().getInt(PARAM_TYPE)]) {
            ContentType.MAILED -> Api.mostMailed()
            ContentType.SHARED -> Api.mostShared()
            ContentType.VIEWED -> Api.mostViewed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.base_content_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recycler.adapter = adapter
        recycler.addItemDecoration(CardViewDecorator(
            requireActivity().resources.getDimensionPixelSize(R.dimen.material_default)))

        swipeRefreshLayout.setOnRefreshListener {
            if (apiCall.isExecuted) {
                apiCall = apiCall.clone()
            }
            apiCall.enqueue(this)
        }

        apiCall.enqueue(this)
        swipeRefreshLayout.isRefreshing = true
    }

    fun reselected() {
        recycler?.smoothScrollToPosition(0)
    }

    override fun onResponse(call: Call<ArticlesResponse>?, response: Response<ArticlesResponse>) {
        if (response.isSuccessful) {
            response.body()?.results?.let { adapter.submitList(it) }
        } else {
            toast(R.string.toast_cant_refresh)
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onFailure(call: Call<ArticlesResponse>?, t: Throwable?) {
        toast(R.string.toast_cant_refresh)
        swipeRefreshLayout.isRefreshing = false
    }

    companion object {

        const val PARAM_TYPE = "type"

        fun create(type: ContentType): BaseContentFragment =
            BaseContentFragment().apply {
                arguments = bundleOf(PARAM_TYPE to type.ordinal)
            }
    }
}

private fun Fragment.toast(@StringRes resId: Int) =
    Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show()
