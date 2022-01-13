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
import cz.nedbalek.nytimessample.databinding.BaseContentFragmentBinding
import cz.nedbalek.nytimessample.ui.activity.DetailActivity
import cz.nedbalek.nytimessample.ui.adapter.ArticlesAdapter
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType
import cz.nedbalek.nytimessample.ui.helpers.CardViewDecorator
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

    private var _binding: BaseContentFragmentBinding? = null
    private val binding: BaseContentFragmentBinding
        get() = requireNotNull(_binding)

    private val adapter by lazy {
        ArticlesAdapter(requireActivity()) { DetailActivity.create(requireActivity(), it) }
    }

    private lateinit var apiCall: Call<ArticlesResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        apiCall = when (ContentType.values()[requireArguments().getInt(PARAM_TYPE)]) {
            ContentType.MAILED -> Api.mostMailed()
            ContentType.SHARED -> Api.mostShared()
            ContentType.VIEWED -> Api.mostViewed()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, state: Bundle?) =
        BaseContentFragmentBinding.inflate(inflater, container, false).run {
            _binding = this
            root
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recycler.adapter = adapter
            recycler.addItemDecoration(CardViewDecorator(
                requireActivity().resources.getDimensionPixelSize(R.dimen.material_default)))

            swipeRefreshLayout.setOnRefreshListener {
                if (apiCall.isExecuted) {
                    apiCall = apiCall.clone()
                }
                apiCall.enqueue(this@BaseContentFragment)
            }

            swipeRefreshLayout.isRefreshing = true
        }

        apiCall.enqueue(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun reselected() {
        binding.recycler.smoothScrollToPosition(0)
    }

    override fun onResponse(call: Call<ArticlesResponse>?, response: Response<ArticlesResponse>) {
        if (response.isSuccessful) {
            response.body()?.results?.let { adapter.submitList(it) }
        } else {
            toast(R.string.toast_cant_refresh)
        }
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onFailure(call: Call<ArticlesResponse>?, t: Throwable?) {
        toast(R.string.toast_cant_refresh)
        binding.swipeRefreshLayout.isRefreshing = false
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
