package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.shared.presentation.viewmodel.CitiesViewModel as SharedCitiesViewModel

class CitiesAndroidViewModel(
    sharedViewModel: SharedCitiesViewModel
) : ViewModel() {

    val uiState = sharedViewModel.uiState
}