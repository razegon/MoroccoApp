package com.rezegon.shared.domain.model

data class WikimediaImage(
    val title: String,
    val url: String,
    val width: Int,
    val height: Int,
    val author: String?,
    val license: String?,
    val licenseUrl: String?,
    val filePageUrl: String?,
    val credit: String?
)