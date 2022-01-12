package cz.nedbalek.nytimessample.connection

import cz.nedbalek.nytimessample.viewobjects.Article

/**
 * Created by prasniatko on 10/07/2017.
 */
data class ArticlesResponse(
    val status: String,
    val num_results: Long,
    val results: List<Article>
)