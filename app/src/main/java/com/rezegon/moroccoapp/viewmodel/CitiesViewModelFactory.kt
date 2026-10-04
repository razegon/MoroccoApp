package com.rezegon.moroccoapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.data.repository.PlacesRepositoryImpl
import com.rezegon.shared.data.source.AndroidJsonResourceReader
import com.rezegon.shared.data.source.JsonPlaceDataSource

class CitiesViewModelFactory(
    context: Context
) : ViewModelProvider.Factory {

    private val applicationContext = context.applicationContext

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CitiesViewModel::class.java)) {

            val resourceReader =
                AndroidJsonResourceReader(applicationContext)

            val dataSource =
                JsonPlaceDataSource(resourceReader)

            val repository =
                PlacesRepositoryImpl(dataSource)

            @Suppress("UNCHECKED_CAST")
            return CitiesViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}