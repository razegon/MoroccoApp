package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.shared.domain.repository.WikimediaRepository

// Android ViewModel factory providing the repository to WikimediaAndroidViewModel.
class WikimediaViewModelFactory(
    private val repository: WikimediaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(WikimediaAndroidViewModel::class.java)) {
            return WikimediaAndroidViewModel(
                repository = repository
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }
}