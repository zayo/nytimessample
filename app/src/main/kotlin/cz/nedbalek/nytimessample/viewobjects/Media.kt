package cz.nedbalek.nytimessample.viewobjects

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

/**
 * Created by prasniatko on 10/07/2017.
 */
data class Media(
        val type: String,
        val subtype: String,
        val caption: String,
        val copyright: String,
        @Json(name = "media-metadata") val metadata: List<Metadata>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            mutableListOf<Metadata>().apply {
                parcel.readTypedList(this, Metadata.CREATOR)
            })

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(subtype)
        parcel.writeString(caption)
        parcel.writeString(copyright)
        parcel.writeTypedList(metadata)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Media> {
        override fun createFromParcel(parcel: Parcel): Media {
            return Media(parcel)
        }

        override fun newArray(size: Int): Array<Media?> {
            return arrayOfNulls(size)
        }
    }
}