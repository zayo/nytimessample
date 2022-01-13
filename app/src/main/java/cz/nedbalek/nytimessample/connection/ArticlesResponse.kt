package cz.nedbalek.nytimessample.connection

import com.squareup.moshi.JsonClass
import cz.nedbalek.nytimessample.viewobjects.Article

/**
 * Created by prasniatko on 10/07/2017.
 */
@JsonClass(generateAdapter = true)
data class ArticlesResponse(
    val status: String,
    val num_results: Long,
    val results: List<Article>
)