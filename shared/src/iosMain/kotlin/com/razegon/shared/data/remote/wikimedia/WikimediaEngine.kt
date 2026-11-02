package com.razegon.shared.data.remote.wikimedia

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

fun createWikimediaEngine(): HttpClientEngine {
    return Darwin.create()
}