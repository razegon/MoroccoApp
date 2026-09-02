package com.rezegon.moroccoapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.data.api.WikidataRetrofit
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

    // Repository responsible for loading places from the local app data source.
    private val repository: PlacesRepository = PlacesRepositoryImpl()

    // Use the shared application-level dependencies from MyApp.
    private val app = application as MyApp

    // Wikidata repository combines the API, memory cache and persistent Room cache.
    private val wikidataRepository = WikidataRepositoryImpl(
        api = WikidataRetrofit.api,
        cache = app.wikidataCache,
        dao = app.database.wikidataDao()
    )

    // Holds the current state displayed by the City Details screen.
    private val _uiState = MutableStateFlow(CityDetailsUiState())

    val uiState: StateFlow<CityDetailsUiState> = _uiState

    init {

        // Load the selected city and its child places from the local repository.
        val place = repository.getPlace(placeId)
        val children = repository.getChildren(placeId)

        _uiState.value = CityDetailsUiState(
            place = place,
            children = children
        )

        // Load additional Wikidata information in the background.
        viewModelScope.launch {

            place?.wikidataId?.let { wikidataId ->

                val wikidataInfo = wikidataRepository.getInfo(
                    wikidataId
                )

                // Update only the Wikidata part of the UI state.
                _uiState.value = _uiState.value.copy(
                    wikidataInfo = wikidataInfo
                )
            }
        }
    }
}