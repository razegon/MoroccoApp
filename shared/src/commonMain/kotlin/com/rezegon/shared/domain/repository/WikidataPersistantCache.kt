package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.WikidataInfo

interface WikidataPersistentCache {

    suspend fun get(wikidataId: String): WikidataInfo?

    suspend fun put(info: WikidataInfo)
}