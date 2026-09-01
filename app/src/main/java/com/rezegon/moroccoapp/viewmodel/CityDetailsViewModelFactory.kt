package com.rezegon.moroccoapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CityDetailsViewModelFactory(
    private val placeId: Int,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(CityDetailsViewModel::class.java)) {
            return CityDetailsViewModel(
                placeId = placeId,
                application = application
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}