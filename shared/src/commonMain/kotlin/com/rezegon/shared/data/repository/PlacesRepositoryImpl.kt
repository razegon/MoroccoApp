package com.rezegon.shared.data.repository

import com.rezegon.shared.data.source.JsonPlaceDataSource
import com.rezegon.shared.domain.model.PlaceData
import com.rezegon.shared.domain.model.PlaceType
import com.rezegon.shared.domain.repository.PlacesRepository

class PlacesRepositoryImpl(
    dataSource: JsonPlaceDataSource
) : PlacesRepository {

    private val places: List<PlaceData> = dataSource.getPlaces()

    override fun getPlaces(): List<PlaceData> {
        return places
    }

    override fun getPlace(placeId: Int): PlaceData? {
        return places.find { it.place.placeId == placeId }
    }

    override fun getPlacesByType(placeType: PlaceType): List<PlaceData> {
        return places.filter { it.place.placeType == placeType }
    }

    override fun getChildren(parentPlaceId: Int): List<PlaceData> {
        return places.filter { it.place.parentPlaceId == parentPlaceId }
    }

    override fun search(query: String): List<PlaceData> {
        TODO("Search will be implemented later")
    }

}