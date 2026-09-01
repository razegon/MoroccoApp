package com.rezegon.moroccoapp.ui.model

import com.rezegon.moroccoapp.domain.model.Place

data class PlacesUiState(
    val places: List<Place> = emptyList()
)