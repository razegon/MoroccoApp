package com.rezegon.moroccoapp.ui.screens.places

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
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.R
import com.rezegon.moroccoapp.data.api.WikimediaRetrofit
import com.rezegon.moroccoapp.data.repository.WikimediaRepositoryImpl
import com.rezegon.moroccoapp.domain.model.Place
import com.rezegon.moroccoapp.ui.components.PlaceListItem
import com.rezegon.moroccoapp.viewmodel.PlacesViewModel
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModel
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModelFactory

@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    onPlaceClick: (Place) -> Unit
) {
    // ViewModel responsible for loading the list of places.
    val viewModel: PlacesViewModel = viewModel()

    // Access the custom application instance.
    // MyApp contains application-wide dependencies such as
    // the shared Wikimedia memory cache and Room database.
    val application = LocalContext.current.applicationContext as MyApp

    // Repository responsible for loading Wikimedia image metadata.
    //
    // It uses:
    // - Wikimedia API for remote data,
    // - shared memory cache for fast access,
    // - Room DAO for persistent local storage.
    val wikimediaRepository = WikimediaRepositoryImpl(
        api = WikimediaRetrofit.api,
        cache = application.wikimediaCache,
        dao = application.database.wikimediaDao()
    )

    // Factory creates the Wikimedia ViewModel using the repository.
    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaViewModel = viewModel(
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
            uiState.places.forEach { place ->

                place.wikimediaImages
                    .firstOrNull()
                    ?.let { imageRef ->
                        wikimediaViewModel.getImage(imageRef.pageId)
                    }
            }
        }

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(uiState.places) { place ->

                // Prefer the Wikimedia image when its metadata
                // has already been loaded. Fall back to the local image.
                val imageUrl = place.wikimediaImages
                    .firstOrNull()
                    ?.let { imageRef ->
                        wikimediaImages[imageRef.pageId]?.url
                    } ?: place.placeImages.firstOrNull()

                PlaceListItem(
                    place = place,
                    imageUrl = imageUrl,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onPlaceClick
                )
            }
        }
    }
}