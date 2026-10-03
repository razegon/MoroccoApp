package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.domain.repository.QuizRepository

class ArcadeViewModelFactory(
    private val repository: QuizRepository
) : ViewModelProvider.Factory {

    // Creates ArcadeViewModel with the repository required by the game logic.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ArcadeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArcadeViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}