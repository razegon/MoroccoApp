package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel

import com.rezegon.moroccoapp.data.repository.PlacesRepositoryImpl

import com.rezegon.moroccoapp.domain.model.PlaceType
import com.rezegon.moroccoapp.domain.repository.PlacesRepository

import com.rezegon.moroccoapp.ui.model.PlacesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlacesViewModel : ViewModel() {

    private val repository: PlacesRepository = PlacesRepositoryImpl()

//    private val wikimediaRepository: WikimediaRepository =
//        WikimediaRepositoryImpl(
//            api = WikimediaRetrofit.api,
//            cache = WikimediaMemoryCache()
//        )

    private val _uiState = MutableStateFlow(PlacesUiState())

    val uiState: StateFlow<PlacesUiState> = _uiState

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        val places = repository
            .getPlaces()
            .filter { it.placeType != PlaceType.CITY }

        _uiState.value = PlacesUiState(
            places = places
        )
    }
}

