package com.rezegon.moroccoapp.ui.model

import com.rezegon.moroccoapp.domain.model.Place

data class CitiesUiState(
    val places: List<Place> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)