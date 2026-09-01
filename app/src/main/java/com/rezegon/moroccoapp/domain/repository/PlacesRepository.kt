package com.rezegon.moroccoapp.domain.repository

import com.rezegon.moroccoapp.domain.model.Place
import com.rezegon.moroccoapp.domain.model.PlaceType

interface PlacesRepository {

    fun getPlaces(): List<Place>

    fun getPlace(placeId: Int): Place?

    fun getPlacesByType(placeType: PlaceType): List<Place>

    fun getChildren(parentPlaceId: Int): List<Place>

    fun search(query: String): List<Place>

}