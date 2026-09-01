package com.rezegon.moroccoapp.domain.repository

import com.rezegon.moroccoapp.domain.model.WikidataInfo

interface WikidataCache {

    fun get(wikidataId: String): WikidataInfo?

    fun put(info: WikidataInfo)

    fun clear()
}

