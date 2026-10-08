package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.shared.presentation.viewmodel.WikimediaViewModel as SharedWikimediaViewModel

class WikimediaAndroidViewModel(
    private val sharedViewModel: SharedWikimediaViewModel
) : ViewModel() {

    val images = sharedViewModel.images

    fun getImage(pageId: Int) {
        sharedViewModel.getImage(pageId)
    }
}