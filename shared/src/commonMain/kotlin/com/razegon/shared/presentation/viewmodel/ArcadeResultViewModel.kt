package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.model.ArcadeResult
import com.razegon.shared.domain.repository.ArcadeResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ArcadeResultViewModel(
    private val repository: ArcadeResultRepository
) {

    // Holds the current TOP 10 Arcade results for the UI.
    private val _topResults =
        MutableStateFlow<List<ArcadeResult>>(emptyList())

    val topResults: StateFlow<List<ArcadeResult>> = _topResults

    // Saves a completed Arcade game result.
    suspend fun saveResult(result: ArcadeResult) {
        repository.saveResult(result)
    }

    // Loads the best Arcade results from local storage.
    suspend fun loadTopResults() {
        val results = repository.getTopResults(20)

        _topResults.value = results
    }
}