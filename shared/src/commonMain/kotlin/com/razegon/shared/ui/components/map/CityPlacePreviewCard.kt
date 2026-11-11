package com.razegon.shared.ui.components.map

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikimediaImage

@Composable
fun CityPlacePreviewCard(
    placeData: PlaceData?,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader,
    isFullscreen: Boolean = false,
    onDetailsClick: (PlaceData) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedContent(
        targetState = placeData,
        modifier = modifier,
        contentKey = { it?.place?.placeId },
        transitionSpec = {
            (fadeIn() + slideInVertically { it / 2 }) togetherWith
                    (fadeOut() + slideOutVertically { it / 2 })
        },
        label = "poiPreview"
    ) {
            selected ->
        selected?.let {


            placeData?.let { selected ->

                val imageUrl = selected.place.wikimediaImages
                    .firstNotNullOfOrNull { imageRef ->
                        imageRef?.let { wikimediaImages[it.pageId] }
                            ?.let { image ->
                                image.thumbnailUrl ?: image.url
                            }
                    }
                    ?: selected.place.placeImages
                        .firstOrNull { !it.isNullOrBlank() }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(
                            max = if (isFullscreen) 560.dp else 440.dp
                        ),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            imageLoader = imageLoader,
                            contentDescription = selected.content.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(
                                    if (isFullscreen) 116.dp else 96.dp
                                )
                                .clip(RoundedCornerShape(14.dp))
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = selected.place.placeType.name
                                        .replace('_', ' ')
                                        .lowercase()
                                        .replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary
                                )

                                Text(
                                    text = selected.content.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                Text(
                                    text = selected.content.snippet
                                        ?: selected.content.descriptions.firstOrNull()
                                        ?: "",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = if (isFullscreen) 3 else 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Button(
                                onClick = { onDetailsClick(selected) },
                                modifier = Modifier.padding(top = 6.dp)
                            ) {
                                Text("Szczegóły")
                            }
                        }
                    }
                }
            }
        }
    }
}

