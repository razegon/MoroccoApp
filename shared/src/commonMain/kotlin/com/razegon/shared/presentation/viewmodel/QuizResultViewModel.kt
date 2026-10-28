package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.model.QuizResult
import com.razegon.shared.domain.repository.QuizResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class QuizResultViewModel(
    private val repository: QuizResultRepository
) {

    // Contains the current TOP 20 quiz results.
    private val _topResults =
        MutableStateFlow<List<QuizResult>>(emptyList())

    val topResults: StateFlow<List<QuizResult>> = _topResults

    // Saves a completed quiz result in the local database.
    suspend fun saveResult(result: QuizResult) {
        repository.saveResult(result)
    }

    // Loads the best results from the local database.
    suspend fun loadTopResults() {
        val results = repository.getTopResults(20)

        _topResults.value = results
    }
}