package com.rezegon.shared.data.source

import android.content.Context

class AndroidJsonResourceReader(
    private val context: Context
) : JsonResourceReader {

    override fun readText(path: String): String {
        return context.assets
            .open(path)
            .bufferedReader()
            .use { it.readText() }
    }
}