package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.shared.di.AppContainer

class QuizViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    // Android ViewModel factory providing the quiz repository to QuizAndroidViewModel.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(QuizAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return QuizAndroidViewModel(
                repository = appContainer.quizRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}
