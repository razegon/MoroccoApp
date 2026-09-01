package com.rezegon.moroccoapp.ui.screens.citydetails

import android.app.Application
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

    val application = LocalContext.current.applicationContext as Application // <<- pamiętać !!
    val factory = CityDetailsViewModelFactory(
        placeId = placeId,
        application = application
    )

    val viewModel: CityDetailsViewModel = viewModel(
        factory = factory
    )

    val app = application as MyApp
    val wikimediaRepository = WikimediaRepositoryImpl(
        api = WikimediaRetrofit.api,
        cache = app.wikimediaCache
    )

    val wikimediaFactory = WikimediaViewModelFactory(
        wikimediaRepository
    )

    val wikimediaViewModel: WikimediaViewModel = viewModel(
        key = "wikimediaCityDetails",
        factory = wikimediaFactory
    )

    val wikimediaImages by wikimediaViewModel.images.collectAsState()

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.place, uiState.children) {

        uiState.place?.wikimediaImages?.forEach { imageRef ->
            imageRef?.let {
                wikimediaViewModel.getImage(it.pageId)
            }
        }

        uiState.children.forEach { child ->

            child.wikimediaImages
                .firstOrNull()
                ?.let { imageRef ->
                wikimediaViewModel.getImage(imageRef.pageId)
            }
        }
    }

    uiState.place?.let { place ->

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
        ) {

            uiState.wikidataInfo?.let { info ->
                WikidataInfoSection(
                    info = info
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.children) { child ->

                    val imageUrl =
                        child.wikimediaImages
                            .firstOrNull()
                            ?.let { wikimediaImages[it.pageId]?.url }
                            ?: child.placeImages.firstOrNull()

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

