
package com.razegon.shared.presentation.model

import com.razegon.shared.domain.model.PlaceData

data class CityMapUiState(
    val placeData: PlaceData? = null,
    val children: List<PlaceData> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
