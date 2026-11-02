
package com.razegon.shared.data.remote.wikidata

import com.razegon.shared.domain.repository.WikidataDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.JsonObject

class KtorWikidataDataSource(
    private val httpClient: HttpClient
) : WikidataDataSource {

    override suspend fun getEntity(
        wikidataId: String
    ): JsonObject {
        return httpClient.get(
            "https://www.wikidata.org/wiki/Special:EntityData/$wikidataId.json"
        ).body()
    }
}
