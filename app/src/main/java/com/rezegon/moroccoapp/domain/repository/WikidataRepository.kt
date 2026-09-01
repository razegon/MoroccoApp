package com.rezegon.moroccoapp.domain.repository

import com.rezegon.moroccoapp.domain.model.WikidataInfo

interface WikidataRepository {

    suspend fun getInfo(wikidataId: String): WikidataInfo?
}

