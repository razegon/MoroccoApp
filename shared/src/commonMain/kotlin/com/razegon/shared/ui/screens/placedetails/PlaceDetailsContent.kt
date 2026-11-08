
package com.razegon.shared.ui.screens.placedetails

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.ImageLoader
import com.razegon.shared.domain.model.PlaceData
import com.razegon.shared.domain.model.WikimediaImage
import com.razegon.shared.openAppSettings
import com.razegon.shared.ui.components.DetailsPager
import com.razegon.shared.ui.components.createPages
import com.razegon.shared.ui.screens.map.MapLocationButton
import com.razegon.shared.ui.screens.map.PlaceMapSection
import kotlinx.coroutines.awaitCancellation
import org.maplibre.compose.location.rememberDefaultLocationProvider
import org.maplibre.compose.location.rememberLocationState
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf

@Composable
fun PlaceDetailsContent(
    placeData: PlaceData,
    wikimediaImages: Map<Int, WikimediaImage>,
    imageLoader: ImageLoader,
    onOpenFullscreen: () -> Unit,
    isMapFullscreen: Boolean,
    onCloseFullscreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pages = createPages(
        wikimediaImages = wikimediaImages,
        images = placeData.place.placeImages,
        descriptions = placeData.content.descriptions,
        wikimediaRefs = placeData.place.wikimediaImages
    )

    val pagerState = rememberPagerState(
        pageCount = { pages.size }
    )

    val latitude = placeData.place.latitude
    val longitude = placeData.place.longitude
    val zoom = placeData.place.zoom ?: 12f

    var openedAppSettings by remember {
        mutableStateOf(false)
    }

    var locationProviderVersion by remember { mutableIntStateOf(0) }

    val locationState = key(locationProviderVersion) {
        rememberLocationState(
            provider = rememberDefaultLocationProvider(),
            enabled = isMapFullscreen
        )
    }

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && openedAppSettings) {
                openedAppSettings = false
                locationProviderVersion++
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        try {
            awaitCancellation()
        } finally {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val openAppSettings = openAppSettings()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->

        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val normalMapHeight = (maxHeight - 12.dp) * 0.5f

            val mapHeight = animateDpAsState(
                targetValue = if (isMapFullscreen) {
                    maxHeight
                } else {
                    normalMapHeight
                }
            )

            DetailsPager(
                placeData = placeData,
                pages = pages,
                wikimediaImages = wikimediaImages,
                imageLoader = imageLoader,
                pagerState = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(normalMapHeight)
            )

            if (pagerState.currentPage == 0 &&
                latitude != null &&
                longitude != null
            ) {
                PlaceMapSection(
                    latitude = latitude,
                    longitude = longitude,
                    zoom = zoom,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(mapHeight.value)
                        .align(Alignment.BottomCenter),
                    onExpandClick = {
                        if (isMapFullscreen) {
                            onCloseFullscreen()
                        } else {
                            onOpenFullscreen()
                        }
                    },
                    isFullscreen = isMapFullscreen,
                    placeId = placeData.place.placeId,
                    locationState = locationState
                )
            }

            if (isMapFullscreen) {
                MapLocationButton(
                    permission = locationState.permission,
                    snackbarHostState = snackbarHostState,
                    onRequestPermission = {
                        locationState.requestPermission()
                    },
                    onOpenSettings = {
                        openedAppSettings = true
                        openAppSettings()
                    }
                )
            }
        }
    }
}
