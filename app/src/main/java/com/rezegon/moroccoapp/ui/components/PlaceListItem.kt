package com.rezegon.moroccoapp.ui.components

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.R
import com.rezegon.moroccoapp.domain.model.Place

@Composable
fun PlaceListItem(
    place: Place,
    modifier: Modifier = Modifier,
    imageHeight: Dp = 180.dp,
    imageUrl: String? = null,
    onClick: (Place) -> Unit
) {
    // Access the shared application instance.
    val application = LocalContext.current.applicationContext as MyApp

    // Use the single ImageLoader shared by the entire application.
    //
    // This allows PlaceListItem to use the same memory cache,
    // disk cache and HTTP client as the other Wikimedia components.
    val imageLoader = application.wikimediaImageLoader

    Card(
        modifier = modifier.padding(top = 12.dp),
        onClick = { onClick(place) }
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
                error = painterResource(R.drawable.default_image),
                fallback = painterResource(R.drawable.default_image)
            )

            // Displays the place name below the image.
            Text(
                text = stringResource(place.placeName),
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