package com.rezegon.shared.presentation.viewmodel

import com.rezegon.shared.domain.model.PlaceType
import com.rezegon.shared.domain.repository.PlacesRepository
import com.rezegon.shared.presentation.model.CitiesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CitiesViewModel(
    private val repository: PlacesRepository
) {

    private val _uiState = MutableStateFlow(CitiesUiState())

    val uiState: StateFlow<CitiesUiState> = _uiState

    init {

        _uiState.value = CitiesUiState(
            places = repository.getPlacesByType(PlaceType.CITY)
        )
    }
}