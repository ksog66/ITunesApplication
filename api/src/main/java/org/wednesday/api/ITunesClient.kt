package org.wednesday.api

import okhttp3.OkHttpClient
import org.wednesday.api.service.ITunesAPI
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object ITunesClient {

    val okHttpBuilder = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(2, TimeUnit.SECONDS)

    private val retrofit =Retrofit.Builder()
        .baseUrl("https://itunes.apple.com")
        .addConverterFactory(MoshiConverterFactory.create())

    val api =retrofit
        .client(okHttpBuilder.build())
        .build()
        .create(ITunesAPI::class.java)

}