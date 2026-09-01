package com.rezegon.moroccoapp.ui.screens.placedetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.data.api.WikimediaRetrofit
import com.rezegon.moroccoapp.data.cache.WikimediaMemoryCache
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
    val factory = PlaceDetailsViewModelFactory(placeId)

    val viewModel: PlaceDetailsViewModel = viewModel(
        factory = factory
    )

    val application = LocalContext.current.applicationContext as MyApp

    val wikimediaRepository = WikimediaRepositoryImpl(
        api = WikimediaRetrofit.api,
        cache = application.wikimediaCache
    )

    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaViewModel = viewModel(
        key = "wikimediaTest2",
        factory = wikimediaFactory
    )

    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    uiState.place?.let { place ->

        LaunchedEffect(place.wikimediaImages) {

            place.wikimediaImages.forEach{ imageRef ->

                imageRef?.let {
                    wikimediaViewModel.getImage(it.pageId)
                }
            }
        }

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

