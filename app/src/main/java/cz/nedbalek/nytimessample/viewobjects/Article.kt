package cz.nedbalek.nytimessample.viewobjects

import android.os.Parcel
import android.os.Parcelable
import cz.nedbalek.nytimessample.lib.SortedItem

/**
 * Created by prasniatko on 10/07/2017.
 */
data class Article(
        val url: String,
        val section: String,
        val byline: String,
        val title: String,
        val abstract: String,
        val published_date: String,
        val source: String,
        val media: List<Media>?
) : Parcelable, SortedItem<Article> {

    constructor(parcel: Parcel) : this(
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readString()!!,
            mutableListOf<Media>().apply {
                parcel.readTypedList(this, Media.CREATOR)
            })

    fun getImageMetadata() = media?.firstOrNull { it.type == "image" && it.subtype == "photo" }?.metadata?.lastOrNull()

    fun getImageUrl(): String? = getImageMetadata()?.url

    override fun getMappingKey() = url

    override fun compare(item: Article) = published_date.compareTo(item.published_date)

    override fun areContentsTheSame(item: Article) = this == item

    override fun areItemsTheSame(item: Article) = title == item.title && section == item.section
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(section)
        parcel.writeString(byline)
        parcel.writeString(title)
        parcel.writeString(abstract)
        parcel.writeString(published_date)
        parcel.writeString(source)
        parcel.writeTypedList(media)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }
    }
}