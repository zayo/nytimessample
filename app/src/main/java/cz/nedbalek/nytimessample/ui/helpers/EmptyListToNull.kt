package cz.nedbalek.nytimessample.ui.helpers

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.io.IOException
import java.lang.reflect.Type

/**
 * Created by prasniatko on 11/07/2017.
 */
class EmptyListToNull : JsonAdapter.Factory {

    companion object {
        const val TAG = "EmptyListToNull"
    }

    override fun create(type: Type, annotations: Set<Annotation>, moshi: Moshi): JsonAdapter<*>? {
        val rawType = Types.getRawType(type)
        if (!List::class.java.isAssignableFrom(rawType)
            && !Set::class.java.isAssignableFrom(rawType)
            && !rawType.isArray
        ) {
            return null // We only handle arrays
        }
        val delegate = moshi.nextAdapter<Any>(this, type, annotations)
        return object : JsonAdapter<Any>() {
            @Throws(IOException::class)
            override fun fromJson(reader: JsonReader): Any? {
                val peek = reader.peek()
                if (peek !== JsonReader.Token.BEGIN_ARRAY) {
                    Log.e(TAG, "Skipping bad value at path: %s [%s for %s]".format(reader.getPath(), peek, JsonReader.Token.BEGIN_ARRAY))
                    reader.skipValue()
                    return null
                }
                return delegate.fromJson(reader)
            }

            @Throws(IOException::class)
            override fun toJson(writer: JsonWriter, value: Any?) {
                delegate.toJson(writer, value)
            }
        }
    }
}