package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.shared.presentation.viewmodel.PlaceDetailsViewModel as SharedPlaceDetailsViewModel

class PlaceDetailsAndroidViewModel(
    sharedViewModel: SharedPlaceDetailsViewModel
) : ViewModel() {

    val uiState = sharedViewModel.uiState
}