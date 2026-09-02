package com.rezegon.moroccoapp.ui.screens.placedetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.data.api.WikimediaRetrofit
import com.rezegon.moroccoapp.data.repository.WikimediaRepositoryImpl
import com.rezegon.moroccoapp.ui.components.DetailsPager
import com.rezegon.moroccoapp.ui.components.createPages
import com.rezegon.moroccoapp.viewmodel.PlaceDetailsViewModel
import com.rezegon.moroccoapp.viewmodel.PlaceDetailsViewModelFactory
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModel
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModelFactory

@Composable
fun PlaceDetailsScreen(
    placeId: Int
) {

    // Factory creates the ViewModel for the selected place.
    val factory = PlaceDetailsViewModelFactory(placeId)

    val viewModel: PlaceDetailsViewModel = viewModel(
        factory = factory
    )

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
        key = "wikimediaTest2",
        factory = wikimediaFactory
    )

    // Contains Wikimedia metadata currently loaded into memory.
    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    // Contains the current state of the selected place.
    val uiState by viewModel.uiState.collectAsState()

    uiState.place?.let { place ->

        // Request Wikimedia metadata for all images
        // assigned to the current place.
        LaunchedEffect(place.wikimediaImages) {

            place.wikimediaImages.forEach { imageRef ->

                imageRef?.let {
                    wikimediaViewModel.getImage(it.pageId)
                }
            }
        }

        // Build the pages displayed by DetailsPager.
        // Wikimedia metadata provides the remote image URLs,
        // while local images can be used as a fallback.
        val pages = createPages(
            wikimediaImages = wikimediaImages,
            images = place.placeImages,
            descriptions = place.placeDescriptions,
            wikimediaRefs = place.wikimediaImages
        )

        DetailsPager(
            place = place,
            pages = pages,
            wikimediaImages = wikimediaImages
        )
    }
}