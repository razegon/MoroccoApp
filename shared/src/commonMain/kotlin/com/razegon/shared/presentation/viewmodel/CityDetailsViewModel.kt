
package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.repository.PlacesRepository
import com.razegon.shared.domain.repository.WikidataRepository
import com.razegon.shared.presentation.model.CityDetailsUiState
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CityDetailsViewModel(
    private val placeId: Int,
    private val placesRepository: PlacesRepository,
    private val wikidataRepository: WikidataRepository,
    private val scope: CoroutineScope
) {
    private val _uiState = MutableStateFlow(CityDetailsUiState())
    val uiState: StateFlow<CityDetailsUiState> = _uiState.asStateFlow()

    init {
        loadCity()
    }

    private fun loadCity() {
        scope.launch {
            _uiState.value = CityDetailsUiState(isLoading = true)

            try {
                val placeData = placesRepository.getPlace(placeId)
                val children = placesRepository.getChildren(placeId)

                _uiState.value = CityDetailsUiState(
                    placeData = placeData,
                    children = children,
                    isLoading = false
                )

                val wikidataId = placeData?.place?.wikidataId
                if (wikidataId != null) {
                    try {
                        val wikidataInfo = wikidataRepository.getInfo(wikidataId)
                        _uiState.value = _uiState.value.copy(
                            wikidataInfo = wikidataInfo
                        )
                    } catch (e: CancellationException) {
                        throw e
                    } catch (_: Exception) {
                        // Dane miasta pozostają dostępne, nawet jeśli Wikidata nie odpowie.
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.value = CityDetailsUiState(
                    isLoading = false,
                    error = e.message ?: "Nie udało się wczytać szczegółów miasta."
                )
            }
        }
    }
}
