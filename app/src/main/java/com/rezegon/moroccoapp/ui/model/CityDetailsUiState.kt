package com.rezegon.moroccoapp.ui.model

import com.rezegon.moroccoapp.domain.model.WikidataInfo
import com.rezegon.shared.domain.model.PlaceData

data class CityDetailsUiState(
    val placeData: PlaceData? = null,
    val children: List<PlaceData> = emptyList(),
    val wikidataInfo: WikidataInfo? = null
)
