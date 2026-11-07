package com.razegon.shared.domain.config

import com.razegon.shared.domain.model.MapArea
import kotlin.math.PI
import kotlin.math.cos

object MapConfig {

    private const val EARTH_RADIUS_KM = 6371.0088

    const val DEFAULT_RADIUS_KM = 0.5

    const val MIN_ZOOM = 15.0
    const val MAX_ZOOM = 20.0

    const val GIBRALTAR_STRAIT_ID = 4200

    fun areaAround(
        latitude: Double,
        longitude: Double,
        minZoom: Double = MIN_ZOOM
    ): MapArea {
        val latitudeDelta =
            DEFAULT_RADIUS_KM / EARTH_RADIUS_KM * 180.0 / PI

        val longitudeDelta =
            DEFAULT_RADIUS_KM /
                    (EARTH_RADIUS_KM * cos(latitude * PI / 180.0)) *
                    180.0 / PI

        return MapArea(
            south = latitude - latitudeDelta,
            west = longitude - longitudeDelta,
            north = latitude + latitudeDelta,
            east = longitude + longitudeDelta,
            minZoom = minZoom,
            maxZoom = MAX_ZOOM
        )
    }
}