package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.di.AppContainer
import com.rezegon.shared.presentation.viewmodel.CityDetailsViewModel as SharedCityDetailsViewModel

class CityDetailsViewModelFactory(
    private val placeId: Int,
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CityDetailsAndroidViewModel::class.java)) {

            return CityDetailsAndroidViewModel(
                placeId = placeId,
                placesRepository = appContainer.placesRepository,
                wikidataRepository = appContainer.wikidataRepository

            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}