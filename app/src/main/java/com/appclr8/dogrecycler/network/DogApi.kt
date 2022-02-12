package com.appclr8.dogrecycler.network

import com.appclr8.dogrecycler.models.MultiResponse
import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

interface DogApi {

    companion object {
        private const val BASE_URL_ROW = "https://dog.ceo/api/"

        fun create(): DogApi {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(Interceptor { chain ->
                    val request = chain.request()
                    val response = chain.proceed(request)
                    //Log.d(TAG, "response:"+response)
                    response
                })
                .build()

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL_ROW)
                .client(okHttpClient)
                .build()

            return retrofit.create(DogApi::class.java)
        }
    }

    /** GET REQUESTS */

    @GET("breeds/image/random/10/")
    fun getImages(): Call<MultiResponse>

    /** POST REQUESTS */
}