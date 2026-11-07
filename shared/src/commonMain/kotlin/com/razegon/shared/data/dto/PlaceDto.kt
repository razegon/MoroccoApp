package com.razegon.shared.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    val place: PlaceDtoData,
    val content: PlaceContentDto
)

@Serializable
data class PlaceDtoData(
    val placeId: Int,
    val placeType: String,
    val parentPlaceId: Int? = null,

    @SerialName("wikidataId")
    val cityWikidataId: String? = null,

    val urbanAreaWikidataId: String? = null,

    val wikimediaImages: List<WikimediaImageRefDto?> = emptyList(),
    val placeImages: List<String?> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val zoom: Float? = null,
    val mapArea: MapAreaDto? = null
)

@Serializable
data class WikimediaImageRefDto(
    val pageId: Int
)

@Serializable
data class PlaceContentDto(
    val id: String,
    val name: String,
    val translation: String? = null,
    val snippet: String? = null,
    val weather: String? = null,
    val openingHours: String? = null,
    val descriptions: List<String> = emptyList(),
    val fallback: PlaceFallbackDto? = null
)

@Serializable
data class PlaceFallbackDto(
    val population: Long? = null,
    val area: Double? = null,
    val elevation: Double? = null,
    val founded: String? = null
)

@Serializable
data class MapAreaDto(
    val south: Double,
    val west: Double,
    val north: Double,
    val east: Double,
    val minZoom: Double,
    val maxZoom: Double
)