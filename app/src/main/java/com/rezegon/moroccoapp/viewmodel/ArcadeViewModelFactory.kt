package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.shared.domain.repository.QuizRepository

class ArcadeViewModelFactory(
    private val repository: QuizRepository
) : ViewModelProvider.Factory {

    // Creates ArcadeViewModel with the repository required by the game logic.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ArcadeAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArcadeAndroidViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}