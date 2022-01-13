package cz.nedbalek.nytimessample.connection

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Retrofit service for NYTimes API.
 */
interface ServerServices {

    @GET("mostemailed/{section}/{period}.json")
    suspend fun mostMailed(@Path("section") section: String, @Path("period") period: Int)
            : ArticlesResponse

    @GET("mostshared/{section}/{period}.json")
    suspend fun mostShared(@Path("section") section: String, @Path("period") period: Int)
            : ArticlesResponse

    @GET("mostviewed/{section}/{period}.json")
    suspend fun mostViewed(@Path("section") section: String, @Path("period") period: Int)
            : ArticlesResponse
}

