package com.rezegon.shared.presentation.viewmodel

import com.rezegon.shared.domain.model.WikimediaImage
import com.rezegon.shared.domain.repository.WikimediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class WikimediaViewModel(
    private val repository: WikimediaRepository
) {

    private val scope = CoroutineScope(
        SupervisorJob() + Dispatchers.Default
    )

    private val _images =
        MutableStateFlow<Map<Int, WikimediaImage>>(emptyMap())

    val images: StateFlow<Map<Int, WikimediaImage>> = _images

    private val loadingImagesInternal = mutableSetOf<Int>()

    fun getImage(pageId: Int) {

        if (_images.value.containsKey(pageId)) return

        if (!loadingImagesInternal.add(pageId)) return

        scope.launch {
            try {
                requestSemaphore.withPermit {
                    val image = repository.getImage(pageId)

                    if (image != null) {
                        _images.value += (pageId to image)
                    }
                }
            } finally {
                loadingImagesInternal.remove(pageId)
            }
        }
    }

    companion object {
        private val requestSemaphore = Semaphore(3)
    }
}