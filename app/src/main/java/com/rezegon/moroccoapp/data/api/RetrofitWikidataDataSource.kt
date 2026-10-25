
package com.rezegon.moroccoapp.data.api

import com.rezegon.shared.domain.repository.WikidataDataSource

class RetrofitWikidataDataSource(
    private val api: WikidataApi
) : WikidataDataSource {

    override suspend fun getEntity(
        wikidataId: String
    ): Map<String, Any> {
        return api.getEntity(wikidataId)
    }
}


