package com.rezegon.moroccoapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.R
import com.rezegon.moroccoapp.domain.model.Place

@Composable
fun CityListItem(
    place: Place,
    imageUrl: String?,
    onClick: (Place) -> Unit
) {
    // Card represents a single city in the cities list.
    // Clicking the card is delegated to the parent screen.
    Card(
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = { onClick(place) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Access the shared application instance.
            val application = LocalContext.current.applicationContext as MyApp

            // Use the single ImageLoader shared by the entire application.
            //
            // This allows all Wikimedia components to share the same
            // memory cache, disk cache and HTTP client.
            val imageLoader = application.wikimediaImageLoader

            // Display the city's Wikimedia image.
            //
            // The imageUrl is provided by the parent screen after
            // Wikimedia metadata has been loaded.
            // If the URL is missing or loading fails, use the default image.
            AsyncImage(
                modifier = Modifier.size(112.dp),
                model = imageUrl,
                imageLoader = imageLoader,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                error = painterResource(R.drawable.default_image),
                fallback = painterResource(R.drawable.default_image)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(4.dp),
            ) {

                // City name.
                Text(
                    text = stringResource(place.placeName),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Optional translation of the city name.
                place.placeTranslate?.let {
                    Text(
                        text = stringResource(it),
                        style = MaterialTheme.typography.bodyLarge,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                // Optional short description of the city.
                place.placeSnippet?.let {
                    Text(
                        text = stringResource(it),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}