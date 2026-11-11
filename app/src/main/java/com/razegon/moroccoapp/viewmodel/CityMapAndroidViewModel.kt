package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.presentation.viewmodel.CityMapViewModel

class CityMapAndroidViewModel(
    cityId: Int,
    placesRepository: PlacesRepository
) : ViewModel() {

    private val sharedViewModel = CityMapViewModel(
        cityId = cityId,
        placesRepository = placesRepository,
        scope = viewModelScope
    )

    val uiState = sharedViewModel.uiState
}