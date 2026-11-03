package com.razegon.shared.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.razegon.shared.domain.model.PlaceData

@Composable
fun PlaceListItem(
    placeData: PlaceData,
    imageLoader: ImageLoader,
    modifier: Modifier = Modifier,
    imageHeight: Dp = 180.dp,
    imageUrl: String? = null,
    onClick: (PlaceData) -> Unit
) {
    Card(
        modifier = modifier.padding(top = 12.dp),
        onClick = { onClick(placeData) }
    ) {
        Column {

            // Displays the Wikimedia image when imageUrl is available.
            // If loading fails, the default image is shown.
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight),
                model = imageUrl,
                imageLoader = imageLoader,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            // Displays the place name below the image.
            Text(
                text = placeData.content.name,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 5.dp
                    )
            )
        }
    }
}