package com.rezegon.moroccoapp.data.repository

import android.util.Log
import com.rezegon.moroccoapp.domain.model.WikimediaImage
import com.rezegon.moroccoapp.data.api.WikimediaApi
import com.rezegon.moroccoapp.domain.repository.WikimediaCache
import com.rezegon.moroccoapp.domain.repository.WikimediaRepository

class WikimediaRepositoryImpl(
    private val api: WikimediaApi,
    private val cache: WikimediaCache
) : WikimediaRepository {

    override suspend fun getImage(pageId: Int): WikimediaImage? {

        cache.get(pageId)?.let { cachedImage ->

            Log.d(
                "WIKIMEDIA_CACHE",
                "CACHE HIT: $pageId"
            )

            return cachedImage
        }

        Log.d(
            "WIKIMEDIA_CACHE",
            "CACHE MISS: $pageId"
        )

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

            val image = response.query.pages[pageId] ?: return null
            val imageInfo = image.imageInfo.firstOrNull() ?: return null

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