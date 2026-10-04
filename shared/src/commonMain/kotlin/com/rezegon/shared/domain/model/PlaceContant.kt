package com.rezegon.shared.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaceContent(
    val id: String,
    val name: String,
    val translation: String? = null,
    val snippet: String? = null,
    val weather: String? = null,
    val openingHours: String? = null,
    val descriptions: List<String> = emptyList(),
    val fallback: PlaceFallback? = null,
)

@Serializable
data class PlaceFallback(
    val population: Long? = null,
    val area: Double? = null,
    val elevation: Double? = null,
    val founded: String? = null,
)