package com.razegon.shared.data.source

interface JsonResourceReader {
    suspend fun readText(path: String): String
}