package com.rezegon.moroccoapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.rezegon.moroccoapp.MyApp
import com.rezegon.moroccoapp.R
import com.rezegon.moroccoapp.domain.model.Place
import com.rezegon.moroccoapp.domain.model.WikimediaImage
import com.rezegon.moroccoapp.domain.model.WikimediaImageRef
import com.rezegon.moroccoapp.ui.model.DetailsPage

/**
 * Wspólny pager zdjęć używany przez ekrany szczegółów City i Place.
 *
 * Obsługuje zdjęcia Wikimedia oraz zwykłe adresy URL, przewijanie zdjęcia,
 * wskaźnik stron oraz informacje o źródle zdjęcia.
 */
@Composable
fun DetailsPager(
    place: Place,
    pages: List<DetailsPage>,
    wikimediaImages: Map<Int, WikimediaImage>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {

    val pagerState = rememberPagerState(
        pageCount = {
            pages.size
        }
    )

    val imageMaxHeight = 240.dp

    // Access the shared application instance.
    val application = LocalContext.current.applicationContext as MyApp

    // Use the single ImageLoader shared by the entire application.
    //
    // This allows all Wikimedia components to share the same
    // memory cache, disk cache and HTTP client.
    val imageLoader = application.wikimediaImageLoader

    var attributionImage by remember {
        mutableStateOf<WikimediaImage?>(null)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier.fillMaxWidth(),
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

        val imageHeight = if (page == 0) {

            with(LocalDensity.current) {
                (imageMaxHeight.toPx() - scrollState.value)
                    .coerceAtLeast(0f)
                    .toDp()
            }
        } else null

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
                                Modifier.height(imageHeight!!)
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
                        } else ContentScale.Fit,
                        error = painterResource(R.drawable.default_image),
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

                    val wikimediaImage = wikimediaImages.values
                        .firstOrNull { it.url == imageUrl }

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

            /*
             * Wspólne informacje City i Place.
             * Na pierwszej stronie wyświetlamy nazwę,
             * tłumaczenie oraz krótki opis.
             */
            if (page == 0) {

                Text(
                    text = stringResource(place.placeName),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(
                        start = 20.dp,
                        top = 16.dp,
                        end = 20.dp
                    )
                )

                place.placeTranslate?.let {

                    Text(
                        text = stringResource(it),
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = FontStyle.Italic,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                    )
                }

                place.placeSnippet?.let {

                    Text(
                        text = stringResource(it),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(
                            start = 20.dp,
                            end = 20.dp
                        )
                    )
                }

                /*
                 * Dodatkowa zawartość przekazana przez ekran.
                 *
                 * CityDetailsScreen przekazuje tutaj LazyRow
                 * z miejscami należącymi do miasta.
                 *
                 * PlaceDetailsScreen nie przekazuje content,
                 * więc domyślnie nic tutaj nie zostanie wyświetlone.
                 */
                content()
            }

            if (page > 0) {

                detailsPage.description?.let {
                    HtmlText(
                        text = stringResource(it),
                        modifier = Modifier.padding(20.dp),
                        textAlign = TextAlign.Justify
                    )
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

fun createPages(
    wikimediaImages: Map<Int, WikimediaImage>,
    images: List<String?>,
    descriptions: List<Int>,
    wikimediaRefs: List<WikimediaImageRef?>
): List<DetailsPage> {

    val pageCount = maxOf(
        wikimediaRefs.size,
        descriptions.size + 1,
        images.size
    )

    return List(pageCount) { index ->

        val imageUrl = when {
            index < wikimediaRefs.size &&
                    wikimediaRefs[index] != null -> {

                val pageId = wikimediaRefs[index]!!.pageId
                wikimediaImages[pageId]?.url
            }

            else -> {
                images.getOrNull(index)
            }
        }

        DetailsPage(
            imageUrl = imageUrl,
            description = if (index > 0) {
                descriptions.getOrNull(index - 1)
            } else {
                null
            }
        )
    }
}