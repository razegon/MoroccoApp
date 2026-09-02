package com.rezegon.moroccoapp.data.cache

import com.rezegon.moroccoapp.domain.model.WikimediaImage

/**
 * Maps the Room entity to the domain model used by the application.
 *
 * This keeps Room-specific persistence details inside the data layer.
 */
fun WikimediaEntity.toDomain(): WikimediaImage {
    return WikimediaImage(
        title = title,
        url = url,
        width = width,
        height = height,
        author = author,
        license = license,
        licenseUrl = licenseUrl,
        credit = credit,
        filePageUrl = filePageUrl
    )
}

/**
 * Maps the domain model to the Room entity used for local persistence.
 *
 * The pageId uniquely identifies the Wikimedia resource
 * and is therefore used as the primary key.
 */
fun WikimediaImage.toEntity(pageId: Int): WikimediaEntity {
    return WikimediaEntity(
        pageId = pageId,
        title = title,
        url = url,
        width = width,
        height = height,
        author = author,
        license = license,
        licenseUrl = licenseUrl,
        credit = credit,
        filePageUrl = filePageUrl
    )
}

