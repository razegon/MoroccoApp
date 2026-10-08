package com.rezegon.shared.presentation.viewmodel

import com.rezegon.shared.domain.model.PlaceType
import com.rezegon.shared.domain.repository.PlacesRepository
import com.rezegon.shared.presentation.model.PlacesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlacesViewModel(
    private val repository: PlacesRepository
) {

    private val _uiState = MutableStateFlow(PlacesUiState())

    val uiState: StateFlow<PlacesUiState> = _uiState

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        val places = repository
            .getPlaces()
            .filter { it.place.placeType != PlaceType.CITY }

        _uiState.value = PlacesUiState(
            places = places
        )
    }
}