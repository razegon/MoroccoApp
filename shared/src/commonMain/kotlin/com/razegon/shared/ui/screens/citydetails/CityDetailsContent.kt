package com.razegon.shared.ui.screens.citydetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikidataInfo
import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.ui.components.DetailsPager
import com.razegon.shared.ui.components.PlaceListItem
import com.razegon.shared.ui.components.WikidataInfoSection
import com.razegon.shared.ui.components.createPages

@Composable
fun CityDetailsContent(
    placeData: PlaceData,
    children: List<PlaceData>,
    wikidataInfo: WikidataInfo?,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader,
    onPlaceClick: (PlaceData) -> Unit
) {
    val pages = createPages(
        wikimediaImages = wikimediaImages,
        images = placeData.place.placeImages,
        descriptions = placeData.content.descriptions,
        wikimediaRefs = placeData.place.wikimediaImages
    )

    DetailsPager(
        placeData = placeData,
        pages = pages,
        wikimediaImages = wikimediaImages,
        imageLoader = imageLoader
    ) {
        wikidataInfo?.let { info ->
            WikidataInfoSection(info = info)
        }

        LazyRow(
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