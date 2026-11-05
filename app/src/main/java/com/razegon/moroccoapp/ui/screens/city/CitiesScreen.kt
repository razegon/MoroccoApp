package com.razegon.moroccoapp.ui.screens.city

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.razegon.moroccoapp.MyApp
import com.razegon.moroccoapp.viewmodel.CitiesAndroidViewModel
import com.razegon.moroccoapp.viewmodel.CitiesViewModelFactory
import com.razegon.moroccoapp.viewmodel.WikimediaAndroidViewModel
import com.razegon.moroccoapp.viewmodel.WikimediaViewModelFactory
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.ui.screens.cities.CitiesContent
import com.razegon.shared.generated.resources.Res
import com.razegon.shared.generated.resources.cities_title
import org.jetbrains.compose.resources.stringResource

@Composable
fun CitiesScreen(
    modifier: Modifier = Modifier,
    onPlaceClick: (PlaceData) -> Unit
) {
    // Access the Android application instance to obtain shared dependencies
    // and the platform-specific image loader.
    val app = LocalContext.current.applicationContext as MyApp

    // Android ViewModel wrapper providing access to the shared cities state.
    val viewModel: CitiesAndroidViewModel = viewModel(
        factory = CitiesViewModelFactory(app.appContainer)
    )

    val uiState by viewModel.uiState.collectAsState()

    // Obtain the shared Wikimedia repository.
    val wikimediaRepository = app.appContainer.wikimediaRepository

    // Create the Android ViewModel wrapper for Wikimedia image loading.
    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaAndroidViewModel = viewModel(
        key = "wikimediaCities",
        factory = wikimediaFactory
    )

    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    // Load metadata for the first Wikimedia image associated with each city.
    // Re-run when the list of cities changes.
    LaunchedEffect(uiState.places) {
        uiState.places.forEach { placeData ->
            placeData.place.wikimediaImages
                .firstOrNull()
                ?.let { imageRef ->
                    wikimediaViewModel.getImage(imageRef.pageId)
                }
        }
    }

    // Render the shared cities UI with data and platform-specific dependencies.
    CitiesContent(
        places = uiState.places,
        wikimediaImages = wikimediaImages,
        imageLoader = app.wikimediaImageLoader,
        title = stringResource(Res.string.cities_title),
        onPlaceClick = onPlaceClick,
        modifier = modifier.statusBarsPadding()
    )
}