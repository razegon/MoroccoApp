package com.rezegon.shared.data.source

import com.rezegon.shared.data.dto.PlaceDto
import com.rezegon.shared.data.mapper.toDomain
import com.rezegon.shared.domain.model.PlaceData
import kotlinx.serialization.json.Json

class JsonPlaceDataSource(
    private val resourceReader: JsonResourceReader
) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    fun getPlaces(): List<PlaceData> {
        val text = resourceReader.readText("morocco.json")

        val placeDto = json.decodeFromString<PlaceDto>(text)

        return listOf(placeDto.toDomain())
    }
}