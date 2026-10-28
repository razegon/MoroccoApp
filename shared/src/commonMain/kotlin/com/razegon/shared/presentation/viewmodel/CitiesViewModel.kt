
package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.model.PlaceType
import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.presentation.model.CitiesUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CitiesViewModel(
    private val repository: PlacesRepository,
    private val scope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(CitiesUiState())
    val uiState: StateFlow<CitiesUiState> = _uiState.asStateFlow()

    init {
        loadCities()
    }

    fun loadCities() {
        scope.launch {
            _uiState.value = CitiesUiState(isLoading = true)

            try {
                val places = repository.getPlacesByType(PlaceType.CITY)
                _uiState.value = CitiesUiState(
                    places = places,
                    isLoading = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CitiesUiState(
                    isLoading = false,
                    error = e.message ?: "Nie udało się wczytać miast."
                )
            }
        }
    }
}
