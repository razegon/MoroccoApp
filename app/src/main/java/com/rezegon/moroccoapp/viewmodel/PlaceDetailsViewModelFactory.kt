package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.di.AppContainer
import com.rezegon.shared.presentation.viewmodel.PlaceDetailsViewModel as SharedPlaceDetailsViewModel

class PlaceDetailsViewModelFactory(
    private val placeId: Int,
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(PlaceDetailsAndroidViewModel::class.java)) {

            return PlaceDetailsAndroidViewModel(
                sharedViewModel = SharedPlaceDetailsViewModel(
                    placeId = placeId,
                    repository = appContainer.placesRepository
                )
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}