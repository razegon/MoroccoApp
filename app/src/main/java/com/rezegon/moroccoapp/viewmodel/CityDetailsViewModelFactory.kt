package com.rezegon.moroccoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.data.repository.PlacesRepositoryImpl
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource

class CityDetailsViewModelFactory(
    private val placeId: Int,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CityDetailsViewModel::class.java)) {

            val resourceReader =
                AndroidJsonResourceReader(application)

            val dataSource =
                JsonPlaceDataSource(resourceReader)

            val repository =
                PlacesRepositoryImpl(dataSource)

            return CityDetailsViewModel(
                placeId = placeId,
                application = application,
                repository = repository
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}