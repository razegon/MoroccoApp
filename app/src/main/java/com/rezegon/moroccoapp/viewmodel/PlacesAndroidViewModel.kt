package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.shared.presentation.viewmodel.PlacesViewModel

class PlacesAndroidViewModel(
    sharedViewModel: PlacesViewModel
) : ViewModel() {

    val uiState = sharedViewModel.uiState
}