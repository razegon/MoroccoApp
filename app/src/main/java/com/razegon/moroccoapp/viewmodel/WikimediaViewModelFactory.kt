package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.razegon.shared.domain.repository.WikimediaRepository
import com.razegon.shared.presentation.viewmodel.WikimediaViewModel as SharedWikimediaViewModel

class WikimediaViewModelFactory(
    private val repository: WikimediaRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(
        modelClass: Class<T>
    ): T {
        if (modelClass.isAssignableFrom(WikimediaAndroidViewModel::class.java)) {

            return WikimediaAndroidViewModel(
                sharedViewModel = SharedWikimediaViewModel(
                    repository = repository
                )
            ) as T
        }

        throw IllegalArgumentException(
            "Unknown ViewModel class: ${modelClass.name}"
        )
    }

}
