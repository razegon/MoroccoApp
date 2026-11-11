package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.presentation.model.CityMapUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CityMapViewModel(
    private val cityId: Int,
    private val placesRepository: PlacesRepository,
    private val scope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(CityMapUiState())
    val uiState: StateFlow<CityMapUiState> = _uiState.asStateFlow()

    init {
        loadCityMap()
    }

    private fun loadCityMap() {
        scope.launch {
            _uiState.value = CityMapUiState(isLoading = true)

            try {
                val placeData = placesRepository.getPlace(cityId)
                val children = placesRepository.getChildren(cityId)

                _uiState.value = CityMapUiState(
                    placeData = placeData,
                    children = children,
                    isLoading = false,
                    error = if (placeData == null) {
                        "Nie udało się wczytać danych miasta."
                    } else {
                        null
                    }
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CityMapUiState(
                    isLoading = false,
                    error = e.message
                        ?: "Nie udało się wczytać danych mapy miasta."
                )
            }
        }
    }
}
