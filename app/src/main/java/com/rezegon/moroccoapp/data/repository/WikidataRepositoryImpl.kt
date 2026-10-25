package com.rezegon.moroccoapp.data.repository

import android.util.Log
import com.rezegon.moroccoapp.data.api.WikidataApi
import com.rezegon.moroccoapp.data.cache.toDomain
import com.rezegon.moroccoapp.data.cache.toEntity
import com.rezegon.shared.data.parser.WikidataResponseParser
import com.rezegon.shared.domain.model.WikidataInfo
import com.rezegon.shared.domain.repository.WikidataCache
import com.rezegon.shared.domain.repository.WikidataPersistentCache
import com.rezegon.shared.domain.repository.WikidataRepository

class WikidataRepositoryImpl(
    private val api: WikidataApi,
    private val cache: WikidataCache,
    private val persistentCache: WikidataPersistentCache,
    private val parser: WikidataResponseParser = WikidataResponseParser()
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

        Log.d(
            "WIKIDATA_REPO",
            "START getInfo($wikidataId)"
        )

        // First level cache: fast in-memory cache.
        cache.get(wikidataId)?.let { cachedInfo ->

            Log.d(
                "WIKIDATA_CACHE",
                "MEMORY CACHE HIT: $wikidataId"
            )

            return cachedInfo
        }

        Log.d(
            "WIKIDATA_CACHE",
            "MEMORY CACHE MISS: $wikidataId"
        )

        // Second level cache: persistent Room database.
        try {
            persistentCache.get(wikidataId)?.let { info ->

                Log.d(
                    "WIKIDATA_ROOM",
                    "ROOM CACHE HIT: $wikidataId"
                )

                // Put the data back into memory cache
                // so future requests are faster.
                cache.put(info)

                return info
            }

            Log.d(
                "WIKIDATA_ROOM",
                "ROOM CACHE MISS: $wikidataId"
            )

        } catch (e: Exception) {

            // A Room failure should not prevent us from trying
            // the API when an internet connection is available.
            Log.e(
                "WIKIDATA_ROOM",
                "Błąd odczytu z Room dla $wikidataId",
                e
            )
        }

        // Last level: fetch fresh data from the Wikidata API.
        return try {

            val response = api.getEntity(wikidataId)

            Log.d(
                "WIKIDATA_REPO",
                "API RESPONSE received"
            )

            val info = parser.parse(wikidataId, response)
                ?: return null

            // Persistent cache.
            try {
                persistentCache.put(info)

                Log.d(
                    "WIKIDATA_ROOM",
                    "ROOM CACHE UPDATE: $wikidataId"
                )

            } catch (e: Exception) {

                // The API result is still valid even if
                // storing it locally fails.
                Log.e(
                    "WIKIDATA_ROOM",
                    "Błąd zapisu do Room dla $wikidataId",
                    e
                )
            }

            // Fast in-memory cache.
            cache.put(info)

            Log.d(
                "WIKIDATA_INFO",
                "population=${info.population}, area=${info.area}, " +
                        "inceptionYear=${info.inception?.year}, " +
                        "precision=${info.inception?.precision}, " +
                        "raw=${info.inception?.raw}"
            )

            info

        } catch (e: Exception) {

            Log.e(
                "WIKIDATA_ERROR",
                "Błąd podczas pobierania Wikidata",
                e
            )

            null
        }
    }
}