package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.WikidataInfo

interface WikidataCache {

    fun get(wikidataId: String): WikidataInfo?

    fun put(info: WikidataInfo)

    fun clear()
}