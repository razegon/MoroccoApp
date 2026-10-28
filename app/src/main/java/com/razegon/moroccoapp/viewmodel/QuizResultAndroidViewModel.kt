package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razegon.shared.domain.model.QuizResult
import com.razegon.shared.presentation.viewmodel.QuizResultViewModel as SharedQuizResultViewModel
import kotlinx.coroutines.launch

class QuizResultAndroidViewModel(
    private val sharedViewModel: SharedQuizResultViewModel
) : ViewModel() {

    val topResults = sharedViewModel.topResults

    fun saveResult(result: QuizResult) {
        viewModelScope.launch {
            sharedViewModel.saveResult(result)
        }
    }

    fun loadTopResults() {
        viewModelScope.launch {
            sharedViewModel.loadTopResults()
        }
    }
}

