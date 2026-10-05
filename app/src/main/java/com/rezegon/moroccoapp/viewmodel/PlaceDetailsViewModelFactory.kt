package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.data.repository.PlacesRepositoryImpl
import com.rezegon.moroccoapp.di.AppContainer
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource

class PlaceDetailsViewModelFactory(
    private val placeId: Int,
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(PlaceDetailsViewModel::class.java)) {

            return PlaceDetailsViewModel(
                placeId = placeId,
                repository = appContainer.placesRepository
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}