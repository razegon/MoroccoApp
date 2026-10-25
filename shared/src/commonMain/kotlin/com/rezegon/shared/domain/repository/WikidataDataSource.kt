
package com.rezegon.shared.domain.repository

interface WikidataDataSource {
    suspend fun getEntity(wikidataId: String): Map<String, Any>
}



