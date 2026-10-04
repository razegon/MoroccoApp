package com.rezegon.moroccoapp.ui.model

import com.rezegon.shared.domain.model.PlaceData

data class CitiesUiState(
    val places: List<PlaceData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)