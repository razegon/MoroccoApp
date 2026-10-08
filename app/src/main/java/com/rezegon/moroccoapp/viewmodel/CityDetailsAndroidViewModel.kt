package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.shared.presentation.viewmodel.CityDetailsViewModel as SharedCityDetailsViewModel

class CityDetailsAndroidViewModel(
    sharedViewModel: SharedCityDetailsViewModel
) : ViewModel() {

    val uiState = sharedViewModel.uiState
}