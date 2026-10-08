package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.shared.domain.repository.PlacesRepository
import com.rezegon.moroccoapp.ui.model.PlaceDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlaceDetailsViewModel(
    private val placeId: Int,
    private val repository: PlacesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceDetailsUiState())

    val uiState: StateFlow<PlaceDetailsUiState> = _uiState

    init {
        _uiState.value = PlaceDetailsUiState(
            place = repository.getPlace(placeId)
        )
    }
}