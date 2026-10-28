package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.shared.domain.repository.PlacesRepository
import com.rezegon.shared.presentation.viewmodel.CitiesViewModel
import com.rezegon.shared.presentation.viewmodel.PlaceDetailsViewModel as SharedPlaceDetailsViewModel

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