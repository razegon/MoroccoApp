package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.moroccoapp.di.AppContainer

class ArcadeViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    // Creates ArcadeViewModel with the repository required by the game logic.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ArcadeAndroidViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArcadeAndroidViewModel(
                repository = appContainer.quizRepository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}