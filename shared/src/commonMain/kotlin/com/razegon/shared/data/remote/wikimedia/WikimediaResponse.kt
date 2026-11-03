package com.razegon.shared.data.remote.wikimedia

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WikimediaResponse(
    val query: Query
)

@Serializable
data class Query(
    val pages: Map<Int, Wikimedia>
)

@Serializable
data class Wikimedia(
    val title: String,

    @SerialName("imageinfo")
    val imageInfo: List<ImageInfo> = emptyList()
)

@Serializable
data class ImageInfo(
    val url: String,
    val width: Int,
    val height: Int,

    @SerialName("thumburl")
    val thumbnailUrl: String? = null,

    @SerialName("thumbwidth")
    val thumbnailWidth: Int? = null,

    @SerialName("thumbheight")
    val thumbnailHeight: Int? = null,

    @SerialName("descriptionurl")
    val descriptionUrl: String? = null,
    @SerialName("extmetadata")
    val extMetadata: ExtMetadata? = null
)

@Serializable
data class ExtMetadata(
    @SerialName("Artist")
    val artist: MetadataValue? = null,

    @SerialName("LicenseShortName")
    val licenseShortName: MetadataValue? = null,

    @SerialName("LicenseUrl")
    val licenseUrl: MetadataValue? = null,

    @SerialName("Credit")
    val credit: MetadataValue? = null
)

@Serializable
data class MetadataValue(
    val value: String?
)