package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.domain.repository.WikidataRepository
import com.razegon.shared.presentation.viewmodel.CityDetailsViewModel as SharedCityDetailsViewModel


class CityDetailsAndroidViewModel(
    placeId: Int,
    placesRepository: PlacesRepository,
    wikidataRepository: WikidataRepository
) : ViewModel() {

    private val sharedViewModel = SharedCityDetailsViewModel(
        placeId = placeId,
        placesRepository = placesRepository,
        wikidataRepository = wikidataRepository,
        scope = viewModelScope
    )

    val uiState = sharedViewModel.uiState
}