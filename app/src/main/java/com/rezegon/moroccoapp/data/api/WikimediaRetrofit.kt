package com.rezegon.moroccoapp.data.api

import com.rezegon.moroccoapp.data.network.moroccoHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WikimediaRetrofit {

    private const val BASE_URL = "https://commons.wikimedia.org"

    val api: WikimediaApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(moroccoHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WikimediaApi::class.java)
    }
}
