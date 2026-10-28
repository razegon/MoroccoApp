package com.razegon.shared.data.source

import com.razegon.shared.generated.resources.Res

class ComposeJsonResourceReader : JsonResourceReader {

    override suspend fun readText(path: String): String {
        return Res.readBytes(path).decodeToString()
    }
}