package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.shared.domain.repository.ArcadeResultRepository
import com.razegon.shared.presentation.viewmodel.ArcadeResultViewModel

// Android ViewModel factory providing the repository to ArcadeResultViewModel.
class ArcadeResultViewModelFactory(
    private val repository: ArcadeResultRepository
) : ViewModelProvider.Factory {

    // Creates the shared ArcadeResultViewModel and wraps it in an Android adapter.
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