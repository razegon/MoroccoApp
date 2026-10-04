package com.rezegon.shared.data.source

interface JsonResourceReader {
    fun readText(path: String): String
}