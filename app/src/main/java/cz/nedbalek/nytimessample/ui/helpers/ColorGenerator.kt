package cz.nedbalek.nytimessample.ui.helpers

import androidx.annotation.ColorInt
import kotlin.math.abs

private val colors = arrayOf(
    0xffe57373,
    0xfff06292,
    0xffba68c8,
    0xff9575cd,
    0xff7986cb,
    0xff64b5f6,
    0xff4fc3f7,
    0xff4dd0e1,
    0xff4db6ac,
    0xff81c784,
    0xffaed581,
    0xffff8a65,
    0xffd4e157,
    0xffffd54f,
    0xffffb74d,
    0xffa1887f,
    0xff90a4ae
)

/**
 * Gets the given [key] hashed into color.
 */
@ColorInt
fun getColor(key: Any): Int = colors[abs(key.hashCode()) % colors.size].toInt()