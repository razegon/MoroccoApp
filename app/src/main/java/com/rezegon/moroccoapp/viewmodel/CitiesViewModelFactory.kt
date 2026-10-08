package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.di.AppContainer
import com.rezegon.shared.presentation.viewmodel.CitiesViewModel

class CitiesViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CitiesAndroidViewModel::class.java)) {

            @Suppress("UNCHECKED_CAST")
            return CitiesAndroidViewModel(
                sharedViewModel = CitiesViewModel(
                    repository = appContainer.placesRepository
                )
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}