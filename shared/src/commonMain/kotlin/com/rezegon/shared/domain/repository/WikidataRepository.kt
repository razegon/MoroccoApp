package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.WikidataInfo

interface WikidataRepository {

    suspend fun getInfo(wikidataId: String): WikidataInfo?
}