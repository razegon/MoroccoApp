package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.shared.domain.repository.PlacesRepository
import com.rezegon.shared.presentation.viewmodel.CitiesViewModel

class CitiesAndroidViewModel(
    repository: PlacesRepository
) : ViewModel() {

    private val sharedViewModel = CitiesViewModel(
        repository = repository,
        scope = viewModelScope
    )
    val uiState = sharedViewModel.uiState
}