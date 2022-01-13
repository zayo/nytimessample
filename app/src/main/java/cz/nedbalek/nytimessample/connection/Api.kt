package cz.nedbalek.nytimessample.connection

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import cz.nedbalek.nytimessample.ui.helpers.EmptyListToNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Retrofit Api for the NYTimes BE.
 */
object Api {

    private val logger by lazy {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor {
                val apiedUrl = it.request().url
                    .newBuilder()
                    .addQueryParameter("api-key", KEY)
                    .build()
                val newRequest = it.request().newBuilder()
                    .url(apiedUrl)
                    .build()
                it.proceed(newRequest)
            }
            .addInterceptor(logger)
            .cache(null)
            .build()
    }

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .add(EmptyListToNull())
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl("https://api.nytimes.com/svc/mostpopular/v2/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    private val service = retrofit.create<ServerServices>(ServerServices::class.java)

    suspend fun mostMailed() = service.mostMailed("all-sections", 30)
    suspend fun mostShared() = service.mostShared("all-sections", 30)
    suspend fun mostViewed() = service.mostViewed("all-sections", 30)
}

private const val KEY = "lCQfxzIcqYmtLoFBygmvu8WigEtlI5vW"
