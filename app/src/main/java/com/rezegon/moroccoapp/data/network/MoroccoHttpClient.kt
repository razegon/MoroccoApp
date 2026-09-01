package com.rezegon.moroccoapp.data.network

import okhttp3.OkHttpClient

/**
 * Wspólny klient HTTP używany przez aplikację do komunikacji
 * z usługami Wikimedia/Wikidata.
 *
 * User-Agent identyfikuje MoroccoApp przy wykonywaniu żądań HTTP.
 *
 * TODO:
 * Docelowo konfiguracja klienta może zostać przeniesiona
 * do globalnej konfiguracji Application/MyApp razem z innymi
 * wspólnymi elementami infrastruktury aplikacji.
 */
val moroccoHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->

        val request = chain.request()
            .newBuilder()
            .addHeader(
                "User-Agent",
                "MoroccoApp/1.0 (kamilo.tranquilo@gmail.com)"
            )
            .build()

        chain.proceed(request)
    }
    .build()

