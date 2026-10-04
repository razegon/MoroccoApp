package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.data.repository.PlacesRepositoryImpl
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource

class PlaceDetailsViewModelFactory(
    private val placeId: Int,
    private val context: android.content.Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(PlaceDetailsViewModel::class.java)) {

            val resourceReader =
                AndroidJsonResourceReader(context.applicationContext)

            val dataSource =
                JsonPlaceDataSource(resourceReader)

            val repository =
                PlacesRepositoryImpl(dataSource)

            return PlaceDetailsViewModel(
                placeId = placeId,
                repository = repository
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}