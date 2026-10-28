package com.razegon.shared.domain.model

data class Place(
    val placeId: Int,
    val placeType: PlaceType,
    val parentPlaceId: Int? = null,
    val wikidataId: String? = null,
    val wikimediaImages: List<WikimediaImageRef?> = emptyList(),
    val placeImages: List<String?> = emptyList(),
    val latitude: Double? = null,
    val longitude: Double? = null,
    val zoom: Float? = null,
    val showMapButton: Boolean = false,
)