package com.razegon.shared.ui.screens.placedetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.ui.components.DetailsPager
import com.razegon.shared.ui.components.createPages
import com.razegon.shared.ui.screens.map.PlaceMapSection

// Shared place details UI that prepares content pages and displays them in a pager.
// The map is displayed separately from HorizontalPager to avoid gesture conflicts.
@Composable
fun PlaceDetailsContent(
    placeData: PlaceData,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader,
    onOpenMap: (Double, Double, Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = createPages(
        wikimediaImages = wikimediaImages,
        images = placeData.place.placeImages,
        descriptions = placeData.content.descriptions,
        wikimediaRefs = placeData.place.wikimediaImages
    )

    // Pager state is owned by this parent so we can react to the current page.
    val pagerState = rememberPagerState(
        pageCount = { pages.size }
    )

    val latitude = placeData.place.latitude
    val longitude = placeData.place.longitude
    val zoom = placeData.place.zoom ?: 12f

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        DetailsPager(
            placeData = placeData,
            pages = pages,
            wikimediaImages = wikimediaImages,
            imageLoader = imageLoader,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        if (pagerState.currentPage == 0 && latitude != null && longitude != null) {
            Spacer(
                modifier = Modifier.height(12.dp)
            )

            PlaceMapSection(
                latitude = latitude,
                longitude = longitude,
                zoom = zoom,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onExpandClick = {
                    onOpenMap(latitude, longitude, zoom)
                },
                placeId = placeData.place.placeId
            )
        }
    }
}