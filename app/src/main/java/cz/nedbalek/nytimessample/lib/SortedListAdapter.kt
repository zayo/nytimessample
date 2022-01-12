package cz.nedbalek.nytimessample.lib

import androidx.recyclerview.widget.SortedListAdapterCallback

/**
 * Created by prasniatko on 27/02/16.
 */
class SortedListAdapter<T : SortedItem<T>>(
        private val mAdapter: SortedRecyclerAdapter<T, *>,
        private val mSorting: Boolean,
        private val mAscending: Boolean
) : SortedListAdapterCallback<T>(mAdapter) {

    override fun compare(o1: T, o2: T): Int {
        if (!mSorting) {
            return 0
        }
        if (mAscending) {
            return mAdapter.compare(o1, o2)
        } else {
            return mAdapter.compare(o2, o1)
        }
    }

    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
        return mAdapter.areContentsTheSame(oldItem, newItem)
    }

    override fun areItemsTheSame(item1: T, item2: T): Boolean {
        return mAdapter.areItemsTheSame(item1, item2)
    }
}
