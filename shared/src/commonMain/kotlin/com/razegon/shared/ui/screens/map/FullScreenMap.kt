package com.razegon.shared.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.razegon.shared.openAppSettings
import org.maplibre.compose.location.rememberDefaultLocationProvider
import org.maplibre.compose.location.rememberLocationState

@Composable
fun FullscreenMapScreen(
    latitude: Double,
    longitude: Double,
    zoom: Float,
    placeId: Int,
    onBackClick: () -> Unit,
) {
    var openedAppSettings by remember { mutableStateOf(false) }

    val locationState = rememberLocationState(
        provider = rememberDefaultLocationProvider(),
        enabled = true
    )

    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && openedAppSettings) {
                openedAppSettings = false
                onBackClick()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        try {
            kotlinx.coroutines.awaitCancellation()
        } finally {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val openAppSettings = openAppSettings()

    LaunchedEffect(
        locationState.permission,
        locationState.availability,
        locationState.status,
        locationState.lastLocation
    ) {
        println("GPS PERMISSION: ${locationState.permission}")
        println("GPS AVAILABILITY: ${locationState.availability}")
        println("GPS STATUS: ${locationState.status}")
        println("GPS LOCATION: ${locationState.lastLocation}")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            PlaceMapSection(
                latitude = latitude,
                longitude = longitude,
                zoom = zoom,
                modifier = Modifier.fillMaxSize(),
                onExpandClick = onBackClick,
                isFullscreen = true,
                placeId = placeId,
                locationState = locationState
            )
        }

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