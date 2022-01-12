package cz.nedbalek.nytimessample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import cz.nedbalek.nytimessample.R
import cz.nedbalek.nytimessample.connection.Api
import cz.nedbalek.nytimessample.connection.ArticlesResponse
import cz.nedbalek.nytimessample.ui.adapter.ArticlesAdapter
import cz.nedbalek.nytimessample.ui.helpers.CardViewDecorator
import kotlinx.android.synthetic.main.base_content_fragment.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by prasniatko on 10/07/2017.
 */
class BaseContentFragment : Fragment(), Callback<ArticlesResponse> {
    enum class ContentType {
        MAILED,
        SHARED,
        VIEWED
    }

    val adapter by lazy {
        ArticlesAdapter(LayoutInflater.from(activity), activity as? ArticlesAdapter.ArticlesActionListener)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.base_content_fragment, container, false)
    }

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
            response.body()?.results?.let { adapter.swap(it) }
        } else {
            Toast.makeText(activity, getString(R.string.toast_cant_refresh), Toast.LENGTH_SHORT)
                .show()
        }
        swipeRefreshLayout.isRefreshing = false
    }

    override fun onFailure(call: Call<ArticlesResponse>?, t: Throwable?) {
        Toast.makeText(activity, getString(R.string.toast_cant_refresh), Toast.LENGTH_SHORT).show()
        swipeRefreshLayout.isRefreshing = false
    }

    companion object {

        const val TAG = "BaseContentFragment"
        const val PARAM_TYPE = "type"

        fun create(type: ContentType): BaseContentFragment {
            val fragment = BaseContentFragment()
            val bundle = Bundle()
            bundle.putInt(PARAM_TYPE, type.ordinal)
            fragment.arguments = bundle
            return fragment
        }
    }

}
