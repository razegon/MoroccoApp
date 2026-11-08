package com.razegon.shared.domain.model

data class UserLocationState(
    val mode: LocationMode = LocationMode.DISABLED,
    val permissionStatus: LocationPermissionStatus =
        LocationPermissionStatus.NOT_DETERMINED,
    val lastLocation: UserLocation? = null,
    val isInsidePlaceBounds: Boolean = false
)
