package com.rezegon.moroccoapp.data.api

import com.google.gson.annotations.SerializedName

data class WikimediaResponse(
    val query: Query,
)

data class Query(
    val pages: Map<Int, Wikimedia>
)

data class Wikimedia(
    val title: String,

    @SerializedName("imageinfo")
    val imageInfo: List<ImageInfo>
)

data class ImageInfo(
    val url: String,
    val width: Int,
    val height: Int,

    @SerializedName("descriptionurl")
    val descriptionUrl: String?,


    @SerializedName("extmetadata")
    val extMetadata: ExtMetadata?
)

data class ExtMetadata(
    @SerializedName("Artist")
    val artist: MetadataValue?,

    @SerializedName("LicenseShortName")
    val licenseShortName: MetadataValue?,

    @SerializedName("LicenseUrl")
    val licenseUrl: MetadataValue?,

    @SerializedName("Credit")
    val credit: MetadataValue?
)

data class MetadataValue(
    val value: String?
)