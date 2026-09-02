package com.rezegon.moroccoapp.data.cache

import com.rezegon.moroccoapp.domain.model.WikidataInfo
import com.rezegon.moroccoapp.domain.model.WikidataTime

/**
 * Maps the Room entity used for local persistence
 * to the domain model used by the application.
 *
 * This keeps Room-specific data structures outside the domain layer.
 */
fun WikidataEntity.toDomain(): WikidataInfo {
    return WikidataInfo(
        wikidataId = wikidataId,
        population = population,
        area = area,
        elevation = elevation,
        inception = inceptionYear?.let {
            WikidataTime(
                year = it,
                precision = inceptionPrecision,
                raw = inceptionRaw
            )
        }
    )
}

/**
 * Maps the domain model to the Room entity used for local persistence.
 *
 * The database requires a non-null Wikidata ID because it is the primary key,
 * so an object without wikidataId cannot be stored.
 */
fun WikidataInfo.toEntity(): WikidataEntity {
    return WikidataEntity(
        wikidataId = wikidataId ?: error("WikidataInfo must have wikidataId"),
        population = population,
        area = area,
        elevation = elevation,
        inceptionYear = inception?.year,
        inceptionPrecision = inception?.precision,
        inceptionRaw = inception?.raw
    )
}

