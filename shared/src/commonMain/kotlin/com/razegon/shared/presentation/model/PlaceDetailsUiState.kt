package com.razegon.shared.presentation.model

import com.razegon.shared.domain.model.PlaceData

data class PlaceDetailsUiState(
    val place: PlaceData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)