package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.domain.repository.QuizResultRepository

class QuizResultViewModelFactory(
    private val repository: QuizResultRepository
) : ViewModelProvider.Factory {

    // Creates QuizResultViewModel with the required repository.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(QuizResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizResultViewModel(
                repository = repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}