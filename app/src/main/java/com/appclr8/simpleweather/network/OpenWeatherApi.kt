package com.appclr8.simpleweather.network

import io.reactivex.Single
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url
import java.io.IOException
import java.util.concurrent.TimeUnit

interface OpenWeatherApi {

    companion object {
        private const val BASE_URL_ROW = "https://api.openweathermap.org/data/2.5/"

        fun create(): OpenWeatherApi {
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

            return retrofit.create(OpenWeatherApi::class.java)
        }
    }

    /*--------------- GET REQUESTS --------------- */

    @GET("onecall?")
    fun getWeather(@Query("lat") lat : String, @Query("lon") lon : String,@Query("units") units : String, @Query("appid") key : String): Call<com.appclr8.simpleweather.models.Response>
}