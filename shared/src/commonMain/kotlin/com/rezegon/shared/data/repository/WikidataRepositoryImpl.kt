package com.rezegon.shared.data.repository

import com.rezegon.shared.data.parser.WikidataResponseParser
import com.rezegon.shared.domain.model.WikidataInfo
import com.rezegon.shared.domain.repository.WikidataCache
import com.rezegon.shared.domain.repository.WikidataDataSource
import com.rezegon.shared.domain.repository.WikidataPersistentCache
import com.rezegon.shared.domain.repository.WikidataRepository

class WikidataRepositoryImpl(
    private val dataSource: WikidataDataSource,
    private val cache: WikidataCache,
    private val persistentCache: WikidataPersistentCache,
    private val parser: WikidataResponseParser = WikidataResponseParser(),
    private val onError: (String, Exception) -> Unit = { _, _ -> }
) : WikidataRepository {

    /**
     * Returns Wikidata information for the given entity.
     *
     * Data is loaded in the following order:
     * 1. In-memory cache
     * 2. Persistent Room database
     * 3. Wikidata API
     *
     * Data received from the API is stored in both Room and memory cache.
     */
    override suspend fun getInfo(
        wikidataId: String
    ): WikidataInfo? {

        // First level cache: fast in-memory cache.
        cache.get(wikidataId)?.let { cachedInfo ->

            return cachedInfo
        }

        // Second level cache: persistent Room database.
        try {
            persistentCache.get(wikidataId)?.let { info ->

                // Put the data back into memory cache
                // so future requests are faster.
                cache.put(info)

                return info
            }
        } catch (e: Exception) {
            onError("Odczyt trwałego cache Wikidata: $wikidataId", e)
        }

        // Last level: fetch fresh data from the Wikidata API.
        return try {

            val response = dataSource.getEntity(wikidataId)

            val info = parser.parse(wikidataId, response)
                ?: return null

            // Persistent cache.
            try {
                persistentCache.put(info)
            } catch (e: Exception) {
                onError("Zapis trwałego cache Wikidata: $wikidataId", e)
            }

            // Fast in-memory cache.
            cache.put(info)

            info

        } catch (e: Exception) {
            onError("Pobieranie danych Wikidata: $wikidataId", e)

            null
        }
    }
}