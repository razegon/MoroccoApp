package com.razegon.shared.domain.repository

import com.razegon.shared.domain.model.WikidataInfo

interface WikidataPersistentCache {

    suspend fun get(wikidataId: String): WikidataInfo?

    suspend fun put(info: WikidataInfo)
}