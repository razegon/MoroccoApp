
package com.razegon.shared.domain.repository

import kotlinx.serialization.json.JsonObject

interface WikidataDataSource {
    suspend fun getEntity(wikidataId: String): JsonObject
}



