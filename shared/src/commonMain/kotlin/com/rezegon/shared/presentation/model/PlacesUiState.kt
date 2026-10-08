package com.rezegon.shared.presentation.model

import com.rezegon.shared.domain.model.PlaceData

data class PlacesUiState(
    val places: List<PlaceData> = emptyList()
)