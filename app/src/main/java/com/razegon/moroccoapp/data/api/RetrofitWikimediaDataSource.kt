
package com.razegon.moroccoapp.data.api

import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.domain.repository.WikimediaDataSource

class RetrofitWikimediaDataSource(
    private val api: WikimediaApi
) : WikimediaDataSource {

    override suspend fun getImage(pageId: Int): WikimediaImage? {
        val response = api.getImage(pageId = pageId)

        val image = response.query.pages[pageId] ?: return null
        val imageInfo = image.imageInfo.firstOrNull() ?: return null

        return WikimediaImage(
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
    }
}


