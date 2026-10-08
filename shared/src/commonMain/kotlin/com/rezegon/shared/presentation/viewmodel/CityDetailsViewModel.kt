package com.rezegon.shared.presentation.viewmodel

import com.rezegon.shared.domain.repository.PlacesRepository
import com.rezegon.shared.domain.repository.WikidataRepository
import com.rezegon.shared.presentation.model.CityDetailsUiState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CityDetailsViewModel(
    private val placeId: Int,
    private val placesRepository: PlacesRepository,
    private val wikidataRepository: WikidataRepository
) {

    private val scope = CoroutineScope(Dispatchers.Default)

    private val _uiState = MutableStateFlow(CityDetailsUiState())

    val uiState: StateFlow<CityDetailsUiState> = _uiState

    init {
        loadCity()
    }

    private fun loadCity() {
        val placeData = placesRepository.getPlace(placeId)
        val children = placesRepository.getChildren(placeId)

        _uiState.value = CityDetailsUiState(
            placeData = placeData,
            children = children
        )

        placeData?.place?.wikidataId?.let { wikidataId ->
            scope.launch {
                val wikidataInfo = wikidataRepository.getInfo(wikidataId)

                _uiState.value = _uiState.value.copy(
                    wikidataInfo = wikidataInfo
                )
            }
        }
    }
}