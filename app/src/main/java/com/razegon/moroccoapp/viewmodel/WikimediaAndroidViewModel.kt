package com.razegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.razegon.shared.domain.repository.WikimediaRepository
import com.razegon.shared.presentation.viewmodel.WikimediaViewModel as SharedWikimediaViewModel

// Android lifecycle adapter for the shared WikimediaViewModel.
class WikimediaAndroidViewModel(
    repository: WikimediaRepository
) : ViewModel() {

    private val sharedViewModel = SharedWikimediaViewModel(
        repository = repository,
        scope = viewModelScope
    )

    val images = sharedViewModel.images

    fun getImage(pageId: Int) {
        sharedViewModel.getImage(pageId)
    }
}