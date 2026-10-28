package com.razegon.shared.domain.repository

import com.razegon.shared.domain.model.WikidataInfo

interface WikidataRepository {

    suspend fun getInfo(wikidataId: String): WikidataInfo?
}