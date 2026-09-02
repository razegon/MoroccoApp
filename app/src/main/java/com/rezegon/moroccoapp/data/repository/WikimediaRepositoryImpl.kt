package com.rezegon.moroccoapp.data.repository

import android.util.Log
import com.rezegon.moroccoapp.data.api.WikimediaApi
import com.rezegon.moroccoapp.data.cache.WikimediaDao
import com.rezegon.moroccoapp.data.cache.toDomain
import com.rezegon.moroccoapp.data.cache.toEntity
import com.rezegon.moroccoapp.domain.model.WikimediaImage
import com.rezegon.moroccoapp.domain.repository.WikimediaCache
import com.rezegon.moroccoapp.domain.repository.WikimediaRepository

class WikimediaRepositoryImpl(
    private val api: WikimediaApi,
    private val cache: WikimediaCache,
    private val dao: WikimediaDao
) : WikimediaRepository {

    /**
     * Returns image metadata for the given Wikimedia page.
     *
     * Data is loaded in the following order:
     * 1. In-memory cache
     * 2. Persistent Room database
     * 3. Wikimedia API
     *
     * Data received from the API is stored in both Room
     * and the in-memory cache.
     */
    override suspend fun getImage(pageId: Int): WikimediaImage? {

        // First level cache: fast in-memory cache.
        cache.get(pageId)?.let { cachedImage ->

            Log.d(
                "WIKIMEDIA_CACHE",
                "MEMORY CACHE HIT: $pageId"
            )

            return cachedImage
        }

        Log.d(
            "WIKIMEDIA_CACHE",
            "MEMORY CACHE MISS: $pageId"
        )

        // Second level cache: persistent Room database.
        try {

            dao.get(pageId)?.let { entity ->

                val image = entity.toDomain()

                Log.d(
                    "WIKIMEDIA_ROOM",
                    "ROOM CACHE HIT: $pageId"
                )

                // Put the image back into memory cache
                // so future requests are faster.
                cache.put(pageId, image)

                return image
            }

            Log.d(
                "WIKIMEDIA_ROOM",
                "ROOM CACHE MISS: $pageId"
            )

        } catch (e: Exception) {

            // A Room failure should not prevent us
            // from trying the API.
            Log.e(
                "WIKIMEDIA_ROOM",
                "Błąd odczytu z Room dla pageId=$pageId",
                e
            )
        }

        // Last level: fetch image metadata from Wikimedia API.
        return try {

            Log.d(
                "WIKIMEDIA_REQUEST",
                "START API pageId=$pageId"
            )

            val response = api.getImage(pageId = pageId)

            Log.d(
                "WIKIMEDIA_REQUEST",
                "END API pageId=$pageId"
            )

            val image = response.query.pages[pageId]
                ?: return null

            val imageInfo = image.imageInfo.firstOrNull()
                ?: return null

            val result = WikimediaImage(
                title = image.title,
                url = imageInfo.url,
                width = imageInfo.width,
                height = imageInfo.height,
                author = imageInfo.extMetadata?.artist?.value
                    ?.replace(Regex("<[^>]*>"), "")
                    ?.trim(),
                license = imageInfo.extMetadata?.licenseShortName?.value,
                licenseUrl = imageInfo.extMetadata?.licenseUrl?.value,
                credit = imageInfo.extMetadata?.credit?.value
                    ?.replace(Regex("<[^>]*>"), "")
                    ?.trim(),
                filePageUrl = imageInfo.descriptionUrl
            )

            // Persist image metadata in Room.
            try {

                dao.upsert(result.toEntity(pageId))

                Log.d(
                    "WIKIMEDIA_ROOM",
                    "ROOM CACHE UPDATE: $pageId"
                )

            } catch (e: Exception) {

                // The API result is still valid even if
                // local persistence fails.
                Log.e(
                    "WIKIMEDIA_ROOM",
                    "Błąd zapisu do Room dla pageId=$pageId",
                    e
                )
            }

            // Store the result in the fast in-memory cache.
            cache.put(pageId, result)

            result

        } catch (e: Exception) {

            Log.e(
                "WIKIMEDIA_ERROR",
                "Błąd pobierania obrazu pageId=$pageId",
                e
            )

            null
        }
    }
}