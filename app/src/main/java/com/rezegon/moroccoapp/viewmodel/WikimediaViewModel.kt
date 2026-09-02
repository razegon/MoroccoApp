package com.rezegon.moroccoapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.moroccoapp.domain.model.WikimediaImage
import com.rezegon.moroccoapp.domain.repository.WikimediaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit

class WikimediaViewModel(
    private val repository: WikimediaRepository
) : ViewModel() {

    // Contains Wikimedia image metadata loaded during the current
    // ViewModel lifetime.
    private val _images =
        MutableStateFlow<Map<Int, WikimediaImage>>(emptyMap())

    val images: StateFlow<Map<Int, WikimediaImage>> = _images

    // Tracks requests that are already in progress.
    //
    // This prevents the same pageId from being requested more than once
    // while its previous request is still running.
    private val loadingImagesInternal = mutableSetOf<Int>()

    /**
     * Loads Wikimedia image metadata for the given pageId.
     *
     * The loading order itself is handled by the repository:
     *
     * MemoryCache -> Room -> Wikimedia API
     *
     * The ViewModel additionally:
     * - prevents duplicate requests for the same pageId,
     * - limits concurrent requests to three,
     * - exposes successfully loaded images through StateFlow.
     */
    fun getImage(pageId: Int) {

        // The image is already available in this ViewModel.
        // No additional request is necessary.
        if (_images.value.containsKey(pageId)) return

        // A request for this pageId is already in progress.
        // Do not start another one.
        if (!loadingImagesInternal.add(pageId)) return

        viewModelScope.launch {

            try {

                // Allow a maximum of three Wikimedia repository
                // operations to run concurrently.
                requestSemaphore.withPermit {

                    val image = repository.getImage(pageId)

                    if (image != null) {
                        _images.value =
                            _images.value + (pageId to image)
                    }

                    Log.d(
                        "WIKIMEDIA",
                        """
                        pageId=$pageId
                        title=${image?.title}
                        author=${image?.author}
                        license=${image?.license}
                        licenseUrl=${image?.licenseUrl}
                        credit=${image?.credit}
                        filePageUrl=${image?.filePageUrl}
                        """.trimIndent()
                    )
                }

            } finally {

                // Always remove the pageId from the set of active
                // requests, even when the repository throws an exception.
                loadingImagesInternal.remove(pageId)
            }
        }
    }

    companion object {

        // Limits the number of concurrent Wikimedia repository
        // operations to avoid sending too many requests at once.
        private val requestSemaphore = Semaphore(3)
    }
}