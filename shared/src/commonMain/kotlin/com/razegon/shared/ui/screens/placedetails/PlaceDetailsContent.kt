package com.razegon.shared.ui.screens.placedetails

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil3.ImageLoader
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.ui.components.DetailsPager
import com.razegon.shared.ui.components.createPages

// Shared place details UI that prepares content pages and displays them in a pager.
@Composable
fun PlaceDetailsContent(
    placeData: PlaceData,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier
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
        imageLoader = imageLoader,
        modifier = modifier
    )
}