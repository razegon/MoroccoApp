package com.rezegon.moroccoapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.moroccoapp.domain.model.QuizResult
import com.rezegon.moroccoapp.domain.repository.QuizResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuizResultViewModel(
    private val repository: QuizResultRepository
) : ViewModel() {

    // Contains the current TOP 10 quiz results.
    private val _topResults =
        MutableStateFlow<List<QuizResult>>(emptyList())

    val topResults: StateFlow<List<QuizResult>> = _topResults

    // Saves a completed quiz result in the local database.
    fun saveResult(result: QuizResult) {
        viewModelScope.launch {
            repository.saveResult(result)

                    Log.d(
                    "QUIZ_RESULT",
                "Saved result: ${result.nickname} ${result.score}/${result.totalQuestions}"
            )
        }
    }

    // Loads the TOP 10 results from the local database.
    fun loadTopResults() {
        viewModelScope.launch {
            val results = repository.getTopResults(10)

            _topResults.value = results

            Log.d(
                "QUIZ_RESULT",
                "Loaded ${results.size} results: $results"
            )
        }
    }
}