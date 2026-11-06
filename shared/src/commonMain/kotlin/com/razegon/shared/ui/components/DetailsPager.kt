package com.razegon.shared.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import com.razegon.shared.data.parser.DescriptionParser
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.domain.model.WikimediaImageRef
import com.razegon.shared.generated.resources.Res
import com.razegon.shared.generated.resources.default_image
import com.razegon.shared.ui.model.DetailsPage
import org.jetbrains.compose.resources.painterResource

// Shared image pager used by city and place details screens.
// Supports Wikimedia images, descriptions, page indicators and image attribution.
@Composable
fun DetailsPager(
    placeData: PlaceData,
    pages: List<DetailsPage>,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader,
    pagerState: PagerState = rememberPagerState(
        pageCount = { pages.size }
    ),
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    val imageMaxHeight = 256.dp

    var attributionImage by remember {
        mutableStateOf<WikimediaImage?>(null)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Top
    ) { page ->
        val detailsPage = pages[page]
        val scrollState = rememberScrollState()

        val imageUrl = detailsPage.imageUrl

        var imageAspectRatio by remember(imageUrl) {
            mutableStateOf<Float?>(null)
        }

        var isImageLoading by remember(imageUrl) {
            mutableStateOf(imageUrl != null)
        }

        val imageHeight = with(LocalDensity.current) {
            (imageMaxHeight.toPx() - scrollState.value)
                .coerceAtLeast(0f)
                .toDp()
        }

        // Parse the description only when its content changes.
        val descriptionBlocks = remember(detailsPage.description) {
            detailsPage.description?.let {
                DescriptionParser.parse(it)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
        ) {
            if (imageUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (page == 0) {
                                Modifier.height(imageHeight)
                            } else {
                                imageAspectRatio?.let {
                                    Modifier.aspectRatio(it)
                                } ?: Modifier
                            }
                        )
                ) {
                    if (isImageLoading) {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(2.dp)
                                .align(Alignment.TopCenter)
                        )
                    }

                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = imageUrl,
                        imageLoader = imageLoader,
                        contentDescription = null,
                        contentScale = if (page == 0) {
                            ContentScale.FillWidth
                        } else {
                            ContentScale.Fit
                        },
                        error = painterResource(Res.drawable.default_image),
                        onLoading = {
                            isImageLoading = true
                        },
                        onSuccess = { state ->
                            isImageLoading = false

                            val size = state.painter.intrinsicSize

                            if (size.width > 0f && size.height > 0f) {
                                imageAspectRatio = size.width / size.height
                            }
                        },
                        onError = {
                            isImageLoading = false
                        }
                    )

                    // Show attribution when the image has Wikimedia metadata.
                    val wikimediaImage = detailsPage.pageId?.let { pageId ->
                        wikimediaImages[pageId]
                    }

                    if (wikimediaImage != null) {
                        WikimediaAttributionIcon(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(
                                    bottom = 16.dp,
                                    end = 8.dp
                                ),
                            onClick = {
                                attributionImage = wikimediaImage
                            }
                        )
                    }

                    if (pages.size > 1) {
                        PageIndicator(
                            pageCount = pages.size,
                            currentPage = pagerState.currentPage,
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 10.dp)
                        )
                    }
                }
            }

            // Display the place name, translation and additional content
            // on the first page only.
            if (page == 0) {
                Text(
                    text = placeData.content.name,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 16.dp,
                        end = 20.dp
                    )
                )

                placeData.content.translation?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                    )
                }

                placeData.content.snippet?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                    )
                }

                // Additional content supplied by the parent screen.
                // City details can use this slot to display child places.
                content()
            }

            if (page > 0) {
                descriptionBlocks?.let {
                    DescriptionContent(blocks = it)
                }
            }
        }
    }

    attributionImage?.let { image ->
        WikimediaAttributionDialog(
            image = image,
            onDismiss = {
                attributionImage = null
            }
        )
    }
}

// Creates pager pages by combining Wikimedia images,
// regular image URLs and descriptions.
fun createPages(
    wikimediaImages: Map<Int, WikimediaImage>,
    images: List<String?>,
    descriptions: List<String>,
    wikimediaRefs: List<WikimediaImageRef?>
): List<DetailsPage> {

    val pageCount = maxOf(
        wikimediaRefs.size,
        descriptions.size + 1,
        images.size
    )

    return List(pageCount) { index ->
        val imageRef = wikimediaRefs.getOrNull(index)

        val imageUrl = imageRef?.let { ref ->
            wikimediaImages[ref.pageId]?.let { image ->
                image.thumbnailUrl ?: image.url
            }
        } ?: images.getOrNull(index)

        DetailsPage(
            imageUrl = imageUrl,
            description = if (index > 0) {
                descriptions.getOrNull(index - 1)
            } else {
                null
            },
            pageId = wikimediaRefs.getOrNull(index)?.pageId
        )
    }
}
