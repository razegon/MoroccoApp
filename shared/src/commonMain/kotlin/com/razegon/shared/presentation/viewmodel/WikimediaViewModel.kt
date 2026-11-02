package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.domain.repository.WikimediaRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
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

    private val loadingImagesInternal = MutableStateFlow<Set<Int>>(emptySet())

    fun getImage(pageId: Int) {

        println("WIKIMEDIA: request pageId=$pageId")

        if (_images.value.containsKey(pageId)) return

        // Atomowo dodajemy ID. Jeśli już istnieje, inne żądanie pobiera ten obraz.
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
                    println("WIKIMEDIA: loading pageId=$pageId")

                    val image = repository.getImage(pageId)

                    println("WIKIMEDIA: result pageId=$pageId, found=${image != null}")

                    if (image != null) {
                        _images.update { current ->
                            val updated = current + (pageId to image)

                            println(
                                "WIKIMEDIA STATE: pageId=$pageId, total=${updated.size}"
                            )

                            updated
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