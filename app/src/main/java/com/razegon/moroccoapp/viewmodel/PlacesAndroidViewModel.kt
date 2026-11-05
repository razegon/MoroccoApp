package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.presentation.viewmodel.PlacesViewModel

// Android lifecycle adapter for the shared PlacesViewModel.
class PlacesAndroidViewModel(
    repository: PlacesRepository
) : ViewModel() {

    private val sharedViewModel = PlacesViewModel(
        repository = repository,
        scope = viewModelScope
    )

    val uiState = sharedViewModel.uiState
}
