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

    suspend fun getPlaces(): List<PlaceData> {
        val files = listOf(
            "ma_cities.json",
            "ma_monuments.json",
            "ma_nature.json"
        )

        return files
            .flatMap { path ->
                val text = resourceReader.readText(path)
                json.decodeFromString<List<PlaceDto>>(text)
            }
            .map { it.toDomain() }
    }
}