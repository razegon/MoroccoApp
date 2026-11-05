package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.shared.di.AppContainer

class PlacesViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    // Android ViewModel factory providing the places repository to PlacesAndroidViewModel.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(PlacesAndroidViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return PlacesAndroidViewModel(
                repository = appContainer.placesRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}