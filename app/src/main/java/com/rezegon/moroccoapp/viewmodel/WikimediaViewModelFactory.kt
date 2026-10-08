package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rezegon.shared.domain.repository.WikimediaRepository
import com.rezegon.shared.presentation.viewmodel.WikimediaViewModel as SharedWikimediaViewModel

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
