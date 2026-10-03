package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.moroccoapp.domain.repository.ArcadeResultRepository

class ArcadeResultViewModelFactory(
    private val repository: ArcadeResultRepository
) : ViewModelProvider.Factory {

    // Creates ArcadeResultViewModel with the repository required for result storage.
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {

        if (modelClass.isAssignableFrom(ArcadeResultViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ArcadeResultViewModel(repository) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}