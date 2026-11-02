package com.razegon.shared.data.remote.wikimedia

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.HttpTimeout

fun createWikimediaHttpClient(
    engine: HttpClientEngine
): HttpClient {
    return HttpClient(engine) {
        install(DefaultRequest) {
            header(
                HttpHeaders.UserAgent,
                "MoroccoApp/1.0 (kamilo.tranquilo@gmail.com)"
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 20_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 20_000
        }

        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                }
            )
        }
    }
}