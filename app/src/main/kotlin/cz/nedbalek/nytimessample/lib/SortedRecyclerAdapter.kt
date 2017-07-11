package cz.nedbalek.nytimessample.lib

import android.support.annotation.CallSuper
import android.support.v4.util.ArrayMap
import android.support.v7.util.SortedList
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import java.util.*

/**
 * Created by prasniatko on 20/09/16.
 */
abstract class SortedRecyclerAdapter<T : SortedItem<T>, VH : RecyclerView.ViewHolder>
/**
 * Basic constructor

 * @param clazz Class of T generic type
 * *
 * @param type see@AdapterType for available types of adapter. Could be one of
 * * UNSORTED, UNSORTED_SELECTABLE, SORTED_ASCENDING, SORTED_DESCENDING, SORTED_ASCENDING_SELECTABLE, SORTED_DESCENDING_SELECTABLE
 */
(clazz: Class<T>, private val mAdapterType: AdapterType) : RecyclerView.Adapter<VH>() {

    enum class AdapterType constructor(val sorting: Boolean, val ascending: Boolean, val selectable: Boolean) {
        UNSORTED(false, false, false),
        SORTED_ASCENDING(true, true, false),
        SORTED_DESCENDING(true, false, false),
        UNSORTED_SELECTABLE(false, false, true),
        SORTED_ASCENDING_SELECTABLE(true, true, true),
        SORTED_DESCENDING_SELECTABLE(true, false, true)
    }

    private val mList: SortedList<T>
    private val mMapping = ArrayMap<Any, T>()
    private val mSortedListAdapter: SortedListAdapter<T>

    private var mSelectedKey: Any? = null

    init {
        this.mSortedListAdapter = SortedListAdapter<T>(this, mAdapterType.sorting, mAdapterType.ascending)
        this.mList = SortedList(clazz, mSortedListAdapter)
    }

    abstract override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH

    @CallSuper
    override fun onBindViewHolder(holder: VH, position: Int) {
        if (mAdapterType.selectable) {
            provideSelectableView(holder, position).isSelected = getItemAt(position).getMappingKey() == mSelectedKey
        }
    }

    open fun provideSelectableView(holder: VH, position: Int): View {
        return holder.itemView
    }

    /**
     * Sorting functions. Override for custom behaviour
     */
    fun getMappingKey(item: T): Any {
        return item.getMappingKey()
    }

    open fun compare(first: T, second: T): Int {
        return first.compare(second)
    }

    fun areContentsTheSame(first: T, second: T): Boolean {
        return first.areContentsTheSame(second)
    }

    fun areItemsTheSame(first: T, second: T): Boolean {
        return first.areItemsTheSame(second)
    }

    override fun getItemCount(): Int {
        return mList.size()
    }

    fun isEmpty(): Boolean {
        return mList.size() == 0
    }

    /**
     * Insert item

     * @param item generic type item to insert
     * *
     * *
     * @return inserted/updated position
     */
    @JvmOverloads
    fun insert(item: T?, notify: Boolean = false): Int {
        if (DEBUG) {
            Log.d(TAG, "insert() called with: item = [$item]")
        }
        if (item == null) {
            return -1
        }
        if (mMapping.containsKey(getMappingKey(item))) {//item already presented
            val old_item = mMapping.get(getMappingKey(item))!!
            if (!areContentsTheSame(old_item, item)) {
                //item update
                return updateById(getMappingKey(old_item), item, notify)
            }
            return mList.indexOf(item)
            // else ignore = same items
        } else {
            // add new item
            mMapping.put(item.getMappingKey(), item)
            val changed = mList.add(item)
            if (notify) {
                notifyItemChanged(changed)
            }
            return changed
        }
    }

    /**
     * Insert collection of items

     * @param items collection
     */
    fun insert(items: List<T>) {
        if (DEBUG) {
            Log.d(TAG, "insert() called with: items = [$items]")
        }
        mList.beginBatchedUpdates()
        var i = 0
        val itemsSize = items.size
        while (i < itemsSize) {
            insert(items[i])
            i++
        }
        mList.endBatchedUpdates()
    }

    /**
     * Merge current list with provided list
     * Remove non existing in provided list
     * Add if not exists in current list
     * Update if it is in both lists
     *
     *
     * Logical functionality is clear -> add all
     * But swap makes this replacement more pretty for user

     * @param items collection of results adapter items
     */
    fun swap(items: List<T>) {
        if (DEBUG) {
            Log.d(TAG, "swap() called with: items = [$items]")
        }

        val idsToRemove = HashSet(mMapping.keys)

        mList.beginBatchedUpdates()
        var i = 0
        val itemsSize = items.size
        while (i < itemsSize) {
            insert(items[i])
            idsToRemove.remove(getMappingKey(items[i]))
            i++
        }

        for (id in idsToRemove) {
            remove(mMapping.get(id))
        }
        mList.endBatchedUpdates()
    }

    /**
     * Clears adapter data
     */
    fun clear() {
        mList.clear()
        mMapping.clear()
    }

    /**
     * Remove item

     * @param item to remove
     * *
     * *
     * @return index of removed item
     */
    @JvmOverloads
    fun remove(item: T?, notify: Boolean = false): Int {
        if (DEBUG) {
            Log.d(TAG, "remove() called with: item = [$item]")
        }
        if (item == null) {
            return -1
        }
        val index = mList.indexOf(item)
        if (index >= 0) {
            mList.removeItemAt(index)
            mMapping.remove(getMappingKey(item))
            if (notify) {
                notifyItemRemoved(index)
            }
        }
        return index
    }

    /**
     * Update item by key

     * @param key of item to update
     * *
     * @param item new item
     * *
     * *
     * @return index of newly inserted/removed/updated item
     */
    @JvmOverloads
    fun updateById(key: Any?, item: T?, notify: Boolean = false): Int {
        if (DEBUG) {
            Log.d(TAG, "updateById() called with: key = [$key], item = [$item]")
        }

        if (key == null) {
            return -1
        }

        val old = mMapping.get(key) ?: return insert(item)

        val index = mList.indexOf(old)
        if (item == null) {
            return remove(old)
        }

        mMapping.put(key, item)
        mList.updateItemAt(index, item)
        if (notify) {
            notifyItemChanged(index)
        }
        return index
    }

    /**
     * Returns if adapter contains specified key

     * @param key requested item key
     * *
     * *
     * @return true if contains
     */
    fun containsKey(key: Any?): Boolean {
        return key != null && mMapping.containsKey(key)
    }

    /**
     * Returns adapter item at provided position

     * @param position requested item position
     * *
     * *
     * @return item
     */
    fun getItemAt(position: Int): T {
        return mList.get(position)
    }

    /**
     * Returns adapter item by provided key

     * @param key requested item key
     * *
     * *
     * @return item
     */
    fun getItemByKey(key: Any?): T? {
        if (key == null) {
            return null
        }
        return mMapping.get(key)
    }

    /**
     * Returns adapter index of provided item

     * @param item requested item
     * *
     * *
     * @return item position
     */
    fun indexOf(item: T): Int {
        return mList.indexOf(item)
    }

    /**
     * Returns adapter index of provided item key

     * @param key requested item key
     * *
     * *
     * @return item key position
     */
    fun indexOf(key: Any): Int {
        return mList.indexOf(getItemByKey(key))
    }

    fun setSelection(key: Any?) {
        if (!mAdapterType.selectable) {
            throw IllegalArgumentException("Use selectable adapter type!")
        }
        if (containsKey(mSelectedKey)) {
            val position = mList.indexOf(mMapping.get(mSelectedKey))
            mSelectedKey = null
            notifyItemChanged(position)
        }
        if (containsKey(key)) {
            mSelectedKey = key
            notifyItemChanged(mList.indexOf(mMapping.get(mSelectedKey)))
        }
    }

    companion object {

        private val TAG = ".SortedRecyclerAdapter"

        private val DEBUG = true
    }
}
