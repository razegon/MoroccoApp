
package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.presentation.model.PlaceDetailsUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlaceDetailsViewModel(
    private val placeId: Int,
    private val repository: PlacesRepository,
    private val scope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(PlaceDetailsUiState())
    val uiState: StateFlow<PlaceDetailsUiState> = _uiState.asStateFlow()

    init {
        loadPlace()
    }

    fun loadPlace() {
        scope.launch {
            _uiState.value = PlaceDetailsUiState(isLoading = true)

            try {
                val place = repository.getPlace(placeId)
                _uiState.value = PlaceDetailsUiState(
                    place = place,
                    isLoading = false
                )
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = PlaceDetailsUiState(
                    isLoading = false,
                    error = e.message ?: "Nie udało się wczytać szczegółów miejsca."
                )
            }
        }
    }
}
