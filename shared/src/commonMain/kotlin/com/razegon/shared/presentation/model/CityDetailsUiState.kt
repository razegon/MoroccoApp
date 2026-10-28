package com.razegon.shared.presentation.model

import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikidataInfo

data class CityDetailsUiState(
    val placeData: PlaceData? = null,
    val children: List<PlaceData> = emptyList(),
    val wikidataInfo: WikidataInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)