package com.razegon.moroccoapp.ui.screens.places

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.razegon.moroccoapp.MyApp
import com.razegon.moroccoapp.R
import com.razegon.moroccoapp.ui.components.PlaceListItem
import com.razegon.moroccoapp.viewmodel.PlacesAndroidViewModel
import com.razegon.moroccoapp.viewmodel.PlacesViewModelFactory
import com.razegon.moroccoapp.viewmodel.WikimediaAndroidViewModel
import com.razegon.moroccoapp.viewmodel.WikimediaViewModelFactory
import com.razegon.shared.domain.model.PlaceData

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    onPlaceClick: (PlaceData) -> Unit
) {
    // Access the custom application instance.
    // MyApp contains application-wide dependencies such as
    // the shared Wikimedia memory cache and Room database.
    val application = LocalContext.current.applicationContext as MyApp

    // ViewModel responsible for loading the list of places.
    val viewModel: PlacesAndroidViewModel = viewModel(
        factory = PlacesViewModelFactory(application.appContainer)
    )

    // Repository responsible for loading Wikimedia image metadata.
    val wikimediaRepository = application.appContainer.wikimediaRepository

    // Factory creates the Wikimedia ViewModel using the repository.
    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaAndroidViewModel = viewModel(
        key = "wikimediaPlaces",
        factory = wikimediaFactory
    )

    // Contains Wikimedia metadata currently loaded into memory.
    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    // Contains the current state of the Places screen.
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxSize()
    ) {

        Text(
            text = stringResource(R.string.places_title),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 16.dp,
                end = 20.dp,
                bottom = 8.dp
            )
        )

        // Request Wikimedia metadata for the first image
        // associated with each place.
        LaunchedEffect(uiState.places) {
            uiState.places.forEach { placeData ->

                placeData.place.wikimediaImages
                    .firstOrNull()
                    ?.let { imageRef ->
                        wikimediaViewModel.getImage(imageRef.pageId)
                    }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(uiState.places) { placeData ->

                // Prefer the Wikimedia image when its metadata
                // has already been loaded. Fall back to the local image.
                val imageUrl = placeData.place.wikimediaImages
                    .firstOrNull()
                    ?.let { imageRef ->
                        wikimediaImages[imageRef.pageId]?.url
                    } ?: placeData.place.placeImages.firstOrNull()

                PlaceListItem(
                    placeData = placeData,
                    imageUrl = imageUrl,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPlaceClick
                )
            }
        }
    }
}