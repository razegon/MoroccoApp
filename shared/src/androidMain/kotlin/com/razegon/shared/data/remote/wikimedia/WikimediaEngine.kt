package com.razegon.shared.data.remote.wikimedia

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

fun createWikimediaEngine(): HttpClientEngine {
    return OkHttp.create()
}