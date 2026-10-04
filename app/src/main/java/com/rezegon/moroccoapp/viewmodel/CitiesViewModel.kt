package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.moroccoapp.data.repository.PlacesRepositoryImpl
import com.rezegon.shared.domain.model.PlaceType
import com.rezegon.moroccoapp.domain.repository.PlacesRepository
import com.rezegon.moroccoapp.ui.model.CitiesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CitiesViewModel(
    private val repository: PlacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CitiesUiState())

    val uiState: StateFlow<CitiesUiState> = _uiState

    init {

        _uiState.value = CitiesUiState(
            places = repository.getPlacesByType(PlaceType.CITY)
        )
    }
}