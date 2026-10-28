package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.presentation.viewmodel.PlaceDetailsViewModel as SharedPlaceDetailsViewModel

class PlaceDetailsAndroidViewModel(
    placeId: Int,
    repository: PlacesRepository,

) : ViewModel() {

    private val sharedViewModel = SharedPlaceDetailsViewModel(
        placeId = placeId,
        repository = repository,
        scope = viewModelScope
    )

    val uiState = sharedViewModel.uiState
}