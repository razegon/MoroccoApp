package com.rezegon.shared.data.source

import com.rezegon.shared.generated.resources.Res

class ComposeJsonResourceReader : JsonResourceReader {

    override suspend fun readText(path: String): String {
        return Res.readBytes(path).decodeToString()
    }
}