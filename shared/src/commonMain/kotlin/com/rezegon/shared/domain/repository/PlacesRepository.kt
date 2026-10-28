package com.rezegon.shared.domain.repository

import com.rezegon.shared.domain.model.PlaceData
import com.rezegon.shared.domain.model.PlaceType

interface PlacesRepository {

    suspend fun getPlaces(): List<PlaceData>

    suspend fun getPlace(placeId: Int): PlaceData?

    suspend fun getPlacesByType(placeType: PlaceType): List<PlaceData>

    suspend fun getChildren(parentPlaceId: Int): List<PlaceData>

    suspend fun search(query: String): List<PlaceData>

}