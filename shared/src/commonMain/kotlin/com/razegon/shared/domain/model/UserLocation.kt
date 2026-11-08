package com.razegon.shared.domain.model

data class UserLocation(
    val latitude: Double,
    val longitude: Double,
    val horizontalAccuracy: Double? = null
)