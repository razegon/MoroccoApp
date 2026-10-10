package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.shared.domain.model.QuizResult
import com.rezegon.shared.presentation.viewmodel.QuizResultViewModel as SharedQuizResultViewModel
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

