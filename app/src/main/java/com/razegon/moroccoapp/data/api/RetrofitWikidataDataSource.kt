
package com.razegon.moroccoapp.data.api

import com.razegon.shared.domain.repository.WikidataDataSource

class RetrofitWikidataDataSource(
    private val api: WikidataApi
) : WikidataDataSource {

    override suspend fun getEntity(
        wikidataId: String
    ): Map<String, Any> {
        return api.getEntity(wikidataId)
    }
}


