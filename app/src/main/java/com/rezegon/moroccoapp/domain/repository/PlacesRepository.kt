package com.rezegon.moroccoapp.domain.repository

import com.rezegon.shared.domain.model.Place
import com.rezegon.shared.domain.model.PlaceData
import com.rezegon.shared.domain.model.PlaceType

interface PlacesRepository {

    fun getPlaces(): List<PlaceData>

    fun getPlace(placeId: Int): PlaceData?

    fun getPlacesByType(placeType: PlaceType): List<PlaceData>

    fun getChildren(parentPlaceId: Int): List<PlaceData>

    fun search(query: String): List<PlaceData>

}