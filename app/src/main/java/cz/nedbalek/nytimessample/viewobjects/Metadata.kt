package cz.nedbalek.nytimessample.viewobjects

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.JsonClass

/**
 * Created by prasniatko on 10/07/2017.
 */
// TODO use @Parcelize or something more up to date.
@JsonClass(generateAdapter = true)
data class Metadata(
    val url: String,
    val format: String,
    val height: Int,
    val width: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(format)
        parcel.writeInt(height)
        parcel.writeInt(width)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Metadata> {
        override fun createFromParcel(parcel: Parcel): Metadata {
            return Metadata(parcel)
        }

        override fun newArray(size: Int): Array<Metadata?> {
            return arrayOfNulls(size)
        }
    }
}