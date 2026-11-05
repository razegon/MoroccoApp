package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.domain.repository.WikimediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

// Shared ViewModel responsible for loading and caching Wikimedia images.
class WikimediaViewModel(
    private val repository: WikimediaRepository,
    private val scope: CoroutineScope
) {

    private val _images = MutableStateFlow<Map<Int, WikimediaImage>>(emptyMap())
    val images: StateFlow<Map<Int, WikimediaImage>> = _images.asStateFlow()

    private val loadingImagesInternal = MutableStateFlow<Set<Int>>(emptySet())

    fun getImage(pageId: Int) {

        if (_images.value.containsKey(pageId)) return

        // Atomically register the page ID to prevent duplicate requests.
        while (true) {
            val current = loadingImagesInternal.value

            if (pageId in current) return

            if (loadingImagesInternal.compareAndSet(current, current + pageId)) {
                break
            }
        }

        scope.launch {
            try {
                requestSemaphore.withPermit {
                    val image = repository.getImage(pageId)

                    if (image != null) {
                        _images.update { current ->
                            current + (pageId to image)
                        }
                    }
                }
            } finally {
                loadingImagesInternal.update { it - pageId }
            }
        }
    }

    companion object {
        private val requestSemaphore = Semaphore(5)
    }
}