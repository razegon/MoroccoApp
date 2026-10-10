package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.shared.domain.repository.QuizRepository
import com.rezegon.shared.presentation.viewmodel.QuizViewModel

class QuizViewModelFactory(
    private val quizRepository: QuizRepository
) : ViewModelProvider.Factory {

    // Creates QuizViewModel and injects the repository it needs.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(QuizAndroidViewModel::class.java)) {
            val sharedViewModel = QuizViewModel(
                repository = quizRepository
            )

            @Suppress("UNCHECKED_CAST")
            return QuizAndroidViewModel(sharedViewModel) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
