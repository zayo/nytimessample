package cz.nedbalek.nytimessample.connection

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by prasniatko on 19/05/2017.
 */
interface ServerServices {

    @GET("mostemailed/{section}/{period}.json")
    fun mostMailed(@Path("section") section: String, @Path("period") period: Int): Call<ArticlesResponse>

    @GET("mostshared/{section}/{period}.json")
    fun mostShared(@Path("section") section: String, @Path("period") period: Int): Call<ArticlesResponse>

    @GET("mostviewed/{section}/{period}.json")
    fun mostViewed(@Path("section") section: String, @Path("period") period: Int): Call<ArticlesResponse>
}

