package com.razegon.shared.domain.repository

import com.razegon.shared.domain.model.WikidataInfo

interface WikidataCache {

    fun get(wikidataId: String): WikidataInfo?

    fun put(info: WikidataInfo)

    fun clear()
}