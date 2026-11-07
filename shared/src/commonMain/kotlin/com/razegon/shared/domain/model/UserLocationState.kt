package com.razegon.shared.domain.model

import org.maplibre.compose.location.LocationMeasurement

data class UserLocationState(
    val mode: LocationMode = LocationMode.DISABLED,
    val permissionStatus: LocationPermissionStatus =
        LocationPermissionStatus.NOT_DETERMINED,
    val lastLocation: LocationMeasurement? = null,
    val isInsidePlaceBounds: Boolean = false
)
