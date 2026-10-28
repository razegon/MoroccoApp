package com.razegon.shared.data.repository

import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.domain.repository.WikimediaCache
import com.razegon.shared.domain.repository.WikimediaDataSource
import com.razegon.shared.domain.repository.WikimediaPersistentCache
import com.razegon.shared.domain.repository.WikimediaRepository

class WikimediaRepositoryImpl(
    private val dataSource: WikimediaDataSource,
    private val cache: WikimediaCache,
    private val persistentCache: WikimediaPersistentCache,
    private val onError: (String, Exception) -> Unit = { _, _ -> }
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

            return cachedImage
        }

        // Second level cache: persistent Room database.
        try {

            persistentCache.get(pageId)?.let { image ->

                // Put the image back into memory cache
                // so future requests are faster.
                cache.put(pageId, image)

                return image
            }

        } catch (e: Exception) {

            // A Room failure should not prevent us
            // from trying the API.
            onError("Błąd odczytu z Room dla pageId=$pageId", e)
        }

        // Last level: fetch image metadata from Wikimedia API.
        return try {

            val result = dataSource.getImage(pageId) ?: return null

            // Persist image metadata in Room.
            try {

                persistentCache.put(pageId, result)

            } catch (e: Exception) {

                // The API result is still valid even if local persistence fails.
                onError("Błąd zapisu do Room dla pageId=$pageId", e)
            }

            // Store the result in the fast in-memory cache.
            cache.put(pageId, result)

            result

        } catch (e: Exception) {

            onError("Błąd pobierania obrazu Wikimedia dla pageId=$pageId", e)

            null
        }
    }
}