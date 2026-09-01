package com.rezegon.moroccoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.data.api.WikidataRetrofit
import com.rezegon.moroccoapp.data.cache.WikidataMemoryCache
import com.rezegon.moroccoapp.data.repository.PlacesRepositoryImpl
import com.rezegon.moroccoapp.data.repository.WikidataRepositoryImpl
import com.rezegon.moroccoapp.domain.repository.PlacesRepository
import com.rezegon.moroccoapp.ui.model.CityDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CityDetailsViewModel(
    private val placeId: Int,
    application: Application
) : AndroidViewModel(application) {

    private val repository: PlacesRepository = PlacesRepositoryImpl()

    private val wikidataRepository = WikidataRepositoryImpl(
        api = WikidataRetrofit.api,
        cache = (application as MyApp)
            .wikidataCache
    )

    private val _uiState = MutableStateFlow(CityDetailsUiState())

    val uiState: StateFlow<CityDetailsUiState> = _uiState

    init {

        val place = repository.getPlace(placeId)
        val children = repository.getChildren(placeId)

        _uiState.value = CityDetailsUiState(
            place = place,
            children = children
        )

        viewModelScope.launch {

            place?.wikidataId?.let { wikidataId ->

                val wikidataInfo = wikidataRepository.getInfo(
                    wikidataId
                )

                _uiState.value = _uiState.value.copy(
                    wikidataInfo = wikidataInfo
                )
            }
        }
    }
}