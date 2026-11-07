package com.razegon.shared.domain.model

data class MapArea(
    val south: Double,
    val west: Double,
    val north: Double,
    val east: Double,
    val minZoom: Double,
    val maxZoom: Double
) {
    init {
        require(south in -90.0..90.0)
        require(north in -90.0..90.0)
        require(west in -180.0..180.0)
        require(east in -180.0..180.0)
        require(south < north)
        require(west < east)
        require(minZoom <= maxZoom)
    }
}
