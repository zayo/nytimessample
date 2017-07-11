package cz.nedbalek.nytimessample.connection

import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import cz.nedbalek.nytimessample.ui.helpers.EmptyListToNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory


/**
 * Created by prasniatko on 19/05/2017.
 */

object Api {

    private const val TAG = "Api"
    private const val KEY = "5860411796e64e7086db5c3f83c48731"

    private val logger by lazy {
        val log = HttpLoggingInterceptor()
        log.level = HttpLoggingInterceptor.Level.BODY
        log
    }


    private val client by lazy {
        OkHttpClient.Builder()
                .addInterceptor {
                    val newRequest = it.request().newBuilder()
                    newRequest.addHeader("api-key", KEY)
                    it.proceed(newRequest.build())
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

    fun mostMailed() = service.mostMailed("all-sections", 30)
    fun mostShared() = service.mostShared("all-sections", 30)
    fun mostViewed() = service.mostViewed("all-sections", 30)

}
