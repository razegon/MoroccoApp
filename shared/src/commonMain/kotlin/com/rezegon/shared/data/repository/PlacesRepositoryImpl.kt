package com.rezegon.shared.data.repository

import com.rezegon.shared.data.source.JsonPlaceDataSource
import com.rezegon.shared.domain.model.PlaceData
import com.rezegon.shared.domain.model.PlaceType
import com.rezegon.shared.domain.repository.PlacesRepository

class PlacesRepositoryImpl(
    private val dataSource: JsonPlaceDataSource
) : PlacesRepository {

    private var cachedPlaces: List<PlaceData>? = null

    private suspend fun allPlaces(): List<PlaceData> {
        return cachedPlaces ?: dataSource.getPlaces().also {
            cachedPlaces = it
        }
    }

    override suspend fun getPlaces(): List<PlaceData> {
        return allPlaces()
    }

    override suspend fun getPlace(placeId: Int): PlaceData? {
        return allPlaces().find { it.place.placeId == placeId }
    }

    override suspend fun getPlacesByType(placeType: PlaceType): List<PlaceData> {
        return allPlaces().filter { it.place.placeType == placeType }
    }

    override suspend fun getChildren(parentPlaceId: Int): List<PlaceData> {
        return allPlaces().filter { it.place.parentPlaceId == parentPlaceId }
    }

    override suspend fun search(query: String): List<PlaceData> {
        TODO("Search will be implemented later")
    }

}