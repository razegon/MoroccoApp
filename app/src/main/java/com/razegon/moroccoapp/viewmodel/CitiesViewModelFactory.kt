package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.shared.di.AppContainer

// Android ViewModel factory providing the places repository to CitiesAndroidViewModel.
class CitiesViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CitiesAndroidViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return CitiesAndroidViewModel(
                repository = appContainer.placesRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}