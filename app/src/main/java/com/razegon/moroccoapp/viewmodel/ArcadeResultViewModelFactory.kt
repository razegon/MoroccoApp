package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.shared.domain.repository.ArcadeResultRepository
import com.razegon.shared.presentation.viewmodel.ArcadeResultViewModel

class ArcadeResultViewModelFactory(
    private val repository: ArcadeResultRepository
) : ViewModelProvider.Factory {

    // Creates ArcadeResultViewModel with the repository required for result storage.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ArcadeResultAndroidViewModel::class.java)) {
            val sharedViewModel = ArcadeResultViewModel(
                repository = repository
            )

            @Suppress("UNCHECKED_CAST")
            return ArcadeResultAndroidViewModel(sharedViewModel) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}