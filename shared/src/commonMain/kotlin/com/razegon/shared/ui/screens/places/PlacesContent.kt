package com.razegon.shared.ui.screens.places

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import coil3.ImageLoader
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.ui.components.PlaceListItem
import kotlinx.coroutines.launch

// Main screen component displaying the places list and search functionality.
@Composable
fun PlacesContent(
    places: List<PlaceData>,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader,
    title: String,
    onPlaceClick: (PlaceData) -> Unit,
    modifier: Modifier = Modifier
) {
    // Stores the text entered by the user in the search field.
    var searchQuery by rememberSaveable {
        mutableStateOf("")
    }

    // Controls whether the search field or the regular title is displayed.
    var isSearchActive by rememberSaveable {
        mutableStateOf(false)
    }

    // Holds places matching the current search query.
    val filteredPlaces by remember(places) {
        derivedStateOf {
            val query = searchQuery.trim()

            if (query.isNotBlank()) {
                places.filter {
                    it.content.name.contains(query, ignoreCase = true)
                }
            } else {
                emptyList()
            }
        }
    }

    // Manages the scroll position of the main LazyColumn.
    val listState = rememberLazyListState()

    // Provides a coroutine scope for scroll operations.
    val coroutineScope = rememberCoroutineScope()

    // Finds the selected place in the original list and scrolls to it.
    fun scrollToPlace(placeData: PlaceData) {
        val index = places.indexOfFirst {
            it.place.placeId == placeData.place.placeId
        }

        // indexOfFirst returns -1 when the item cannot be found.
        if (index >= 0) {
            coroutineScope.launch {
                listState.animateScrollToItem(index)
            }
        }
    }

    // Provides access to the software keyboard and current focus.
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Main screen layout.
    Column(
        modifier = modifier.fillMaxSize()
    ) {

        // Displays either the title with a search icon or the active search field.
        PlacesSearchBar(
            title = title,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            isSearchActive = isSearchActive,
            onSearchActiveChange = { isSearchActive = it },
            onSearch = {
                // Scrolls to the first matching place when search is submitted.
                filteredPlaces.firstOrNull()?.let(::scrollToPlace)
            }
        )

        // Displays search suggestions only when search is active and the query is not blank.
        if (isSearchActive && searchQuery.isNotBlank()) {
            PlacesSearchSuggestions(
                places = filteredPlaces,

                // Selecting a suggestion scrolls to the place without opening its details.
                onPlaceSelected = { placeData ->

                    // Hides the keyboard and clears the search field focus.
                    keyboardController?.hide()
                    focusManager.clearFocus()

                    // Hides the suggestions list.
                    isSearchActive = false

                    // Scrolls to the selected place.
                    scrollToPlace(placeData)
                }
            )
        }

        // Displays the complete list of places.
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Uses a stable place ID as the key for each list item.
            items(
                items = places,
                key = { placeData -> placeData.place.placeId }
            ) { placeData ->

                // Tries to load the first available Wikimedia thumbnail.
                // Falls back to the first image stored in the place data.
                val imageUrl = placeData.place.wikimediaImages
                    .firstOrNull()
                    ?.let { imageRef ->
                        wikimediaImages[imageRef.pageId]?.let { image ->
                            image.thumbnailUrl ?: image.url
                        }
                    } ?: placeData.place.placeImages.firstOrNull()

                // Displays an individual place item.
                PlaceListItem(
                    placeData = placeData,
                    imageLoader = imageLoader,
                    imageUrl = imageUrl,
                    modifier = Modifier.fillMaxWidth(),

                    // Regular list clicks open the place details.
                    onClick = onPlaceClick
                )
            }
        }
    }
}
