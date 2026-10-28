package com.razegon.shared.data.source

import com.razegon.shared.data.dto.PlaceDto
import com.razegon.shared.data.mapper.toDomain
import com.razegon.shared.domain.model.PlaceData
import kotlinx.serialization.json.Json

class JsonPlaceDataSource(
    private val resourceReader: JsonResourceReader
) {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    suspend fun getPlaces(): List<PlaceData> {
        val files = listOf(
            "files/places/ma_cities.json",
            "files/places/ma_monuments.json",
            "files/places/ma_nature.json"
        )

        return files
            .flatMap { path ->
                val text = resourceReader.readText(path)
                json.decodeFromString<List<PlaceDto>>(text)
            }
            .map { it.toDomain() }
    }
}