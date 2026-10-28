package com.rezegon.shared.data.source

interface JsonResourceReader {
    suspend fun readText(path: String): String
}