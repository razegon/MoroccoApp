
package com.razegon.shared.ui.screens.cities

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.ui.components.CityListItem

@Composable
fun CitiesContent(
    places: List<PlaceData>,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader,
    title: String,
    onPlaceClick: (PlaceData) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(
                start = 20.dp,
                top = 16.dp,
                end = 20.dp,
                bottom = 8.dp
            )
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        ) {
            items(
                items = places,
                key = { placeData -> placeData.place.placeId }
            ) { placeData ->

                val imageUrl = placeData.place.wikimediaImages
                    .firstOrNull()
                    ?.let { imageRef ->
                        wikimediaImages[imageRef.pageId]?.let { image ->
                            image.thumbnailUrl ?: image.url
                        }
                    } ?: placeData.place.placeImages.firstOrNull()

                CityListItem(
                    placeData = placeData,
                    imageLoader = imageLoader,
                    imageUrl = imageUrl,
                    onClick = onPlaceClick
                )
            }
        }
    }
}

