package com.rezegon.moroccoapp.ui.screens.citydetails

import android.app.Application
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.data.api.WikimediaRetrofit
import com.rezegon.moroccoapp.data.repository.WikimediaRepositoryImpl
import com.rezegon.moroccoapp.domain.model.Place
import com.rezegon.moroccoapp.ui.components.DetailsPager
import com.rezegon.moroccoapp.ui.components.PlaceListItem
import com.rezegon.moroccoapp.ui.components.WikidataInfoSection
import com.rezegon.moroccoapp.ui.components.createPages
import com.rezegon.moroccoapp.viewmodel.CityDetailsViewModel
import com.rezegon.moroccoapp.viewmodel.CityDetailsViewModelFactory
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModel
import com.rezegon.moroccoapp.viewmodel.WikimediaViewModelFactory

@Composable
fun CityDetailsScreen(
    placeId: Int,
    onPlaceClick: (Place) -> Unit
) {

    // Get the application context as the base Application object.
    // <<- pamiętać !!
    val application = LocalContext.current.applicationContext as Application

    // Factory creates the ViewModel and passes the selected place ID
    // together with the application instance.
    val factory = CityDetailsViewModelFactory(
        placeId = placeId,
        application = application
    )

    val viewModel: CityDetailsViewModel = viewModel(
        factory = factory
    )

    // MyApp contains application-wide dependencies,
    // including the shared Wikimedia memory cache and Room database.
    val app = application as MyApp

    // Repository responsible for loading Wikimedia image metadata.
    //
    // It uses:
    // - Wikimedia API for remote data,
    // - shared memory cache for fast access,
    // - Room DAO for persistent local storage.
    val wikimediaRepository = WikimediaRepositoryImpl(
        api = WikimediaRetrofit.api,
        cache = app.wikimediaCache,
        dao = app.database.wikimediaDao()
    )

    // Factory creates the Wikimedia ViewModel using the repository above.
    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaViewModel = viewModel(
        key = "wikimediaCityDetails",
        factory = wikimediaFactory
    )

    // Contains all Wikimedia metadata already loaded into memory.
    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    // Contains the state of the selected city and its child places.
    val uiState by viewModel.uiState.collectAsState()

    // Request Wikimedia metadata whenever the current city
    // or its child places change.
    LaunchedEffect(uiState.place, uiState.children) {

        // Load all Wikimedia images assigned to the city itself.
        uiState.place?.wikimediaImages?.forEach { imageRef ->

            imageRef?.let {
                wikimediaViewModel.getImage(it.pageId)
            }
        }

        // Load the first Wikimedia image for each child place.
        uiState.children.forEach { child ->

            child.wikimediaImages
                .firstOrNull()
                ?.let { imageRef ->
                    wikimediaViewModel.getImage(imageRef.pageId)
                }
        }
    }

    // Render the screen only when the selected place is available.
    uiState.place?.let { place ->

        // Build the pages displayed by DetailsPager.
        // Wikimedia URLs are taken from the loaded metadata map,
        // while local images can be used as a fallback.
        val pages = createPages(
            wikimediaImages = wikimediaImages,
            images = place.placeImages,
            descriptions = place.placeDescriptions,
            wikimediaRefs = place.wikimediaImages
        )

        Log.d(
            "WIKIMEDIA_UI",
            "children=${uiState.children.size}, images=${wikimediaImages.keys}"
        )

        DetailsPager(
            place = place,
            pages = pages,
            wikimediaImages = wikimediaImages
        ) {

            // Display additional Wikidata information
            // when it has been successfully loaded.
            uiState.wikidataInfo?.let { info ->
                WikidataInfoSection(
                    info = info
                )
            }

            // Display the places belonging to the current city.
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                items(uiState.children) { child ->

                    // Prefer the Wikimedia image when its metadata
                    // has already been loaded. Fall back to the local image.
                    val imageUrl =
                        child.wikimediaImages
                            .firstOrNull()
                            ?.let { wikimediaImages[it.pageId]?.url }
                            ?: child.placeImages.firstOrNull()

                    Log.d(
                        "WIKIMEDIA_ROW",
                        "child=${child.placeId}, " +
                                "imageRef=${child.wikimediaImages.firstOrNull()?.pageId}, " +
                                "url=$imageUrl"
                    )

                    PlaceListItem(
                        place = child,
                        imageUrl = imageUrl,
                        modifier = Modifier.width(180.dp),
                        imageHeight = 140.dp,
                        onClick = onPlaceClick
                    )
                }
            }
        }
    }
}