package cz.nedbalek.nytimessample.lib

/**
 * Created by prasniatko on 27/02/16.
 */
interface SortedItem<T> {
    fun getMappingKey(): Any

    fun compare(item: T): Int

    fun areContentsTheSame(item: T): Boolean

    fun areItemsTheSame(item: T): Boolean
}
