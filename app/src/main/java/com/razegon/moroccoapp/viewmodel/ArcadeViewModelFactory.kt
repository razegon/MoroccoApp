package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.shared.di.AppContainer

class ArcadeViewModelFactory(
    private val appContainer: AppContainer
) : ViewModelProvider.Factory {

    // Android ViewModel factory providing the shared quiz repository to ArcadeAndroidViewModel.
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