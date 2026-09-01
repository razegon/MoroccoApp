package com.rezegon.moroccoapp.domain.model

import com.rezegon.moroccoapp.R

data class Place(
    // val countryId: Int,
    val placeName: Int,
    val placeId: Int,
    val placeType: PlaceType,
    val parentPlaceId: Int? = null,
    val placeTranslate: Int? = null,
    val placeSnippet: Int? = null,
    val wikimediaImages: List<WikimediaImageRef?> = emptyList(),
    val placeImages: List<String?> = emptyList(),
    val placeDescriptions: List<Int> = emptyList(),
    val wikidataId: String? = null,
    val population: Long? = null,
    val altitude: Int? = null,
    val openingHours: Int? =null,
    val placeIcon: Int = R.drawable.ic_launcher_foreground,
    val placeWeather: Int? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val zoom: Float? = null,
    val showMapButton: Boolean = false,
    val isCity: Boolean = false, // do analizy
)