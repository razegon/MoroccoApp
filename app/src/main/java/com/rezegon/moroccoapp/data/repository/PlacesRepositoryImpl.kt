package com.rezegon.moroccoapp.data.repository

import com.rezegon.moroccoapp.data.datasource.MoroccoDataSource
import com.rezegon.shared.domain.model.Place
import com.rezegon.shared.domain.model.PlaceType
import com.rezegon.moroccoapp.domain.repository.PlacesRepository
import com.rezegon.shared.data.source.JsonPlaceDataSource
import com.rezegon.shared.domain.model.PlaceData

class PlacesRepositoryImpl(
    private val dataSource: JsonPlaceDataSource
) : PlacesRepository {

    override fun getPlaces(): List<PlaceData> {
        return dataSource.getPlaces()
    }

    override fun getPlace(placeId: Int): PlaceData? {
        return dataSource
            .getPlaces()
            .find { it.place.placeId == placeId }
    }

    override fun getPlacesByType(placeType: PlaceType): List<PlaceData> {
        return dataSource
            .getPlaces()
            .filter { it.place.placeType == placeType }
    }

    override fun getChildren(parentPlaceId: Int): List<PlaceData> {
        return dataSource
            .getPlaces()
            .filter { it.place.parentPlaceId == parentPlaceId }
    }

    override fun search(query: String): List<PlaceData> {
        TODO("Search will be implemented later")
    }

}