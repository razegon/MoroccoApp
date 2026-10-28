package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.moroccoapp.di.AppContainer

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
                placeId = placeId,
                repository = appContainer.placesRepository

            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}