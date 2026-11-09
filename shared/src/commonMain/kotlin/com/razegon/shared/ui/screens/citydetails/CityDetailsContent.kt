package com.razegon.shared.ui.screens.citydetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikidataInfo
import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.ui.components.DetailsPager
import com.razegon.shared.ui.components.PlaceListItem
import com.razegon.shared.ui.components.WikidataInfoSection
import com.razegon.shared.ui.components.createPages
import com.razegon.shared.ui.screens.map.CityMapSection

// Shared city details UI with a separate pager and child places list/map area.

@Composable
fun CityDetailsContent(
    placeData: PlaceData,
    children: List<PlaceData>,
    wikidataInfo: WikidataInfo?,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader,
    onPlaceClick: (PlaceData) -> Unit,
    isMapFullscreen: Boolean,
    onOpenFullscreen: () -> Unit,
    onCloseFullscreen: () -> Unit
) {
    var showMap by remember { mutableStateOf(true) }

    val pages = createPages(
        wikimediaImages = wikimediaImages,
        images = placeData.place.placeImages,
        descriptions = placeData.content.descriptions,
        wikimediaRefs = placeData.place.wikimediaImages
    )

    val pagerState = rememberPagerState(
        pageCount = { pages.size }
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // Scrollable details and Wikidata section
        DetailsPager(
            placeData = placeData,
            pages = pages,
            wikimediaImages = wikimediaImages,
            imageLoader = imageLoader,
            pagerState = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            wikidataInfo?.let { info ->
                WikidataInfoSection(info = info)
            }
        }

        // Map / list switch
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mapa")

            Switch(
                checked = showMap,
                onCheckedChange = { showMap = it },
                modifier = Modifier.scale(0.75f)
            )
        }

        // Map or children list
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (isMapFullscreen || pagerState.currentPage == 0) {
                if (showMap) {
                    CityMapSection(
                        placeData = placeData,
                        children = children,
                        modifier = Modifier.fillMaxSize(),
                        onPlaceClick = onPlaceClick,
                        onExpandClick = {
                            if (isMapFullscreen) {
                                onCloseFullscreen()
                            } else {
                                onOpenFullscreen()
                            }
                        },
                        isFullscreen = isMapFullscreen
                    )
                } else if (!isMapFullscreen) {
                    LazyRow(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(children) { child ->
                            val imageUrl = child.place.wikimediaImages
                                .firstOrNull()
                                ?.let { imageRef ->
                                    wikimediaImages[imageRef.pageId]?.let { image ->
                                        image.thumbnailUrl ?: image.url
                                    }
                                } ?: child.place.placeImages.firstOrNull()

                            PlaceListItem(
                                placeData = child,
                                imageLoader = imageLoader,
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
    }
}
