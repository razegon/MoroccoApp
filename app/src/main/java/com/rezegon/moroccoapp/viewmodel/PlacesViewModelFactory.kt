package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.di.AppContainer
import com.rezegon.shared.presentation.viewmodel.PlacesViewModel

class PlacesViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(PlacesAndroidViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return PlacesAndroidViewModel(
                sharedViewModel = PlacesViewModel(
                    repository = appContainer.placesRepository
                )
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}