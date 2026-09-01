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
import kotlin.collections.emptySet

class WikimediaViewModel(
    private val repository: WikimediaRepository
) : ViewModel() {

    private val _images = MutableStateFlow<Map<Int, WikimediaImage>>(emptyMap())

    val images: StateFlow<Map<Int, WikimediaImage>> = _images

    private val _loadingImages = MutableStateFlow<Set<Int>>(emptySet())

    val loadingImages: StateFlow<Set<Int>> = _loadingImages

    private val loadingImagesInternal = mutableSetOf<Int>()

    /**
     * Pobiera informacje o obrazie Wikimedia.
     *
     * Zapobiega wielokrotnemu pobieraniu tego samego pageId
     * oraz ogranicza liczbę równoczesnych requestów do trzech.
     */
    fun getImage(pageId: Int) {

        if (_images.value.containsKey(pageId)) return

        if (!loadingImagesInternal.add(pageId)) return

        _loadingImages.value = loadingImagesInternal.toSet()

        viewModelScope.launch {

            try {

                // Ograniczenie do maksymalnie 3 równoczesnych requestów
                requestSemaphore.withPermit {

                    val image = repository.getImage(pageId)

                    if (image != null) {
                        _images.value = images.value + (pageId to image)
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
                loadingImagesInternal.remove(pageId)
                _loadingImages.value = loadingImagesInternal.toSet()
            }

        }
    }

    companion object {
        private val requestSemaphore = Semaphore(3)
    }
}
