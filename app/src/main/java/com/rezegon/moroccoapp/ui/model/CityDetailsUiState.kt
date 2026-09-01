package com.rezegon.moroccoapp.ui.model

import com.rezegon.moroccoapp.domain.model.Place
import com.rezegon.moroccoapp.domain.model.WikidataInfo

data class CityDetailsUiState(
    val place: Place? = null,
    val children: List<Place> = emptyList(),
    val wikidataInfo: WikidataInfo? = null
)
