package com.razegon.shared.data.remote.wikimedia

import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.domain.repository.WikimediaDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

private fun String.stripHtml(): String =
    replace(Regex("<[^>]*>"), "").trim()

class KtorWikimediaDataSource(
    private val httpClient: HttpClient
) : WikimediaDataSource {

    override suspend fun getImage(pageId: Int): WikimediaImage? {
        val response: WikimediaResponse = httpClient.get(
            "https://commons.wikimedia.org/w/api.php"
        ) {
            parameter("action", "query")
            parameter("pageids", pageId)
            parameter("prop", "imageinfo")
            parameter("iiprop", "url|size|extmetadata")
            parameter("iiurlwidth", 1200)
            parameter("format", "json")
        }.body()

        val page = response.query.pages[pageId] ?: return null

        val imageInfo = page.imageInfo.firstOrNull() ?: return null

        val metadata = imageInfo.extMetadata

        return WikimediaImage(
            title = page.title,
            url = imageInfo.url,
            width = imageInfo.width,
            height = imageInfo.height,
            thumbnailUrl = imageInfo.thumbnailUrl,
            thumbnailWidth = imageInfo.thumbnailWidth,
            thumbnailHeight = imageInfo.thumbnailHeight,
            author = metadata?.artist?.value?.stripHtml(),
            license = metadata?.licenseShortName?.value,
            licenseUrl = metadata?.licenseUrl?.value,
            filePageUrl = imageInfo.descriptionUrl,
            credit = metadata?.credit?.value?.stripHtml()
        )
    }
}