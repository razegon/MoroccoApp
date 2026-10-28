package com.razegon.shared.data.mapper

import com.razegon.shared.data.dto.PlaceDto
import com.razegon.shared.domain.model.Place
import com.razegon.shared.domain.model.PlaceContent
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.PlaceFallback
import com.razegon.shared.domain.model.PlaceType
import com.razegon.shared.domain.model.WikimediaImageRef

fun PlaceDto.toDomain(): PlaceData {
    return PlaceData(
        place = Place(
            placeId = place.placeId,
            placeType = PlaceType.valueOf(place.placeType),
            parentPlaceId = place.parentPlaceId,
            wikidataId = place.wikidataId,
            wikimediaImages = place.wikimediaImages.map { image ->
                image?.let {
                    WikimediaImageRef(it.pageId)
                }
            },
            placeImages = place.placeImages,
            latitude = place.latitude,
            longitude = place.longitude,
            zoom = place.zoom,
            showMapButton = place.showMapButton
        ),
        content = PlaceContent(
            id = content.id,
            name = content.name,
            translation = content.translation,
            snippet = content.snippet,
            weather = content.weather,
            openingHours = content.openingHours,
            descriptions = content.descriptions,
            fallback = content.fallback?.let {
                PlaceFallback(
                    population = it.population,
                    area = it.area,
                    elevation = it.elevation,
                    founded = it.founded
                )
            }
        )
    )
}