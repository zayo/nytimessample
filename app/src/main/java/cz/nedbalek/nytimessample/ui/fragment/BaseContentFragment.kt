package cz.nedbalek.nytimessample.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cz.nedbalek.nytimessample.R
import cz.nedbalek.nytimessample.databinding.BaseContentFragmentBinding
import cz.nedbalek.nytimessample.ui.activity.DetailActivity
import cz.nedbalek.nytimessample.ui.adapter.ArticlesAdapter
import cz.nedbalek.nytimessample.ui.fragment.BaseContentFragment.ContentType
import cz.nedbalek.nytimessample.ui.helpers.CardViewDecorator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Fragment template for displaying [ContentType] customized screens with content.
 */
class BaseContentFragment : Fragment() {
    enum class ContentType {
        MAILED,
        SHARED,
        VIEWED
    }

    private val adapter by lazy {
        ArticlesAdapter(requireActivity()) { DetailActivity.create(requireActivity(), it) }
    }

    private var _binding: BaseContentFragmentBinding? = null
    private val binding: BaseContentFragmentBinding
        get() = requireNotNull(_binding)

    private val type by lazy {
        ContentType.values()[requireArguments().getInt(PARAM_TYPE)]
    }

    private val viewModel: BaseContentViewModel by viewModels()

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
                viewModel.load(type)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(State.STARTED) {
                viewModel.state.collect(::handleState)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun reselected() {
        binding.recycler.smoothScrollToPosition(0)
    }

    private fun handleState(state: BaseContentViewModel.State) = when (state) {
        is BaseContentViewModel.State.Idle -> {
            viewModel.load(type)
        }

        is BaseContentViewModel.State.Loading -> {
            binding.swipeRefreshLayout.isRefreshing = true
        }

        is BaseContentViewModel.State.Failed -> {
            binding.swipeRefreshLayout.isRefreshing = false
            toast(R.string.toast_cant_refresh)
        }

        is BaseContentViewModel.State.Data -> {
            binding.swipeRefreshLayout.isRefreshing = false
            adapter.submitList(state.articles)
        }
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
