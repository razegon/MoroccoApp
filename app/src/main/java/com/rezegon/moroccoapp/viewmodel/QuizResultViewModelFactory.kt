package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.shared.domain.repository.QuizResultRepository
import com.rezegon.shared.presentation.viewmodel.QuizResultViewModel

class QuizResultViewModelFactory(
    private val repository: QuizResultRepository
) : ViewModelProvider.Factory {

    // Creates QuizResultViewModel with the required repository.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(QuizResultAndroidViewModel::class.java)) {
            val sharedViewModel = QuizResultViewModel(
                repository = repository
            )

            @Suppress("UNCHECKED_CAST")
            return QuizResultAndroidViewModel(sharedViewModel) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}