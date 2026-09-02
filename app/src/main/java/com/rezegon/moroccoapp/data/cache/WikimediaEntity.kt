package com.rezegon.moroccoapp.data.cache

import androidx.room3.Entity
import androidx.room3.PrimaryKey

/**
 * Room entity used to persist Wikimedia image metadata locally.
 *
 * Each Wikimedia page is uniquely identified by its pageId.
 */
@Entity
data class WikimediaEntity(

    @PrimaryKey
    val pageId: Int,

    val title: String,
    val url: String,
    val width: Int,
    val height: Int,
    val author: String?,
    val license: String?,
    val licenseUrl: String?,
    val credit: String?,
    val filePageUrl: String?
)

