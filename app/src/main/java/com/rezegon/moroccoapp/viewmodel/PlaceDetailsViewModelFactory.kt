package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PlaceDetailsViewModelFactory(
    private val placeId: Int
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(PlaceDetailsViewModel::class.java)) {
            return PlaceDetailsViewModel(placeId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}