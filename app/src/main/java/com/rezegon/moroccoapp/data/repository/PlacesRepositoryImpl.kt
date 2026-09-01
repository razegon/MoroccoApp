package com.rezegon.moroccoapp.data.repository

import com.rezegon.moroccoapp.data.datasource.MoroccoDataSource
import com.rezegon.moroccoapp.domain.model.Place
import com.rezegon.moroccoapp.domain.model.PlaceType
import com.rezegon.moroccoapp.domain.repository.PlacesRepository

class PlacesRepositoryImpl : PlacesRepository {

    private val dataSource = MoroccoDataSource

    override fun getPlaces(): List<Place> {
        return dataSource.getPlaces()
    }

    override fun getPlace(placeId: Int): Place? {
        return dataSource
            .getPlaces()
            .find { it.placeId == placeId }
    }

    override fun getPlacesByType(placeType: PlaceType): List<Place> {
        return dataSource
            .getPlaces()
            .filter { it.placeType == placeType }
    }

    override fun getChildren(parentPlaceId: Int): List<Place> {
        return dataSource
            .getPlaces()
            .filter { it.parentPlaceId == parentPlaceId }
    }

    override fun search(query: String): List<Place> {
        TODO("Search will be implemented later")
    }

}