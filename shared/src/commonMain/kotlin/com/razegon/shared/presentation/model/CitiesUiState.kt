package com.razegon.shared.presentation.model

import com.razegon.shared.domain.model.PlaceData

data class CitiesUiState(
    val places: List<PlaceData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)