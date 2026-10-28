
package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.presentation.model.PlacesUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlacesViewModel(
    private val repository: PlacesRepository,
    private val scope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(PlacesUiState())
    val uiState: StateFlow<PlacesUiState> = _uiState.asStateFlow()

    init {
        loadPlaces()
    }

    fun loadPlaces() {
        scope.launch {
            _uiState.value = PlacesUiState(isLoading = true)

            try {
                val places = repository.getPlaces()
                _uiState.value = PlacesUiState(
                    places = places,
                    isLoading = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = PlacesUiState(
                    isLoading = false,
                    error = e.message ?: "Nie udało się wczytać miejsc."
                )
            }
        }
    }
}
