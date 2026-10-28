package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.presentation.viewmodel.CitiesViewModel

class CitiesAndroidViewModel(
    repository: PlacesRepository
) : ViewModel() {

    private val sharedViewModel = CitiesViewModel(
        repository = repository,
        scope = viewModelScope
    )
    val uiState = sharedViewModel.uiState
}