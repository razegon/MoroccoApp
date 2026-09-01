package com.rezegon.moroccoapp.data.api

import com.rezegon.moroccoapp.data.network.moroccoHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WikidataRetrofit {

    private const val BASE_URL = "https://www.wikidata.org"

    val api: WikidataApi by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(moroccoHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WikidataApi::class.java)
    }
}

