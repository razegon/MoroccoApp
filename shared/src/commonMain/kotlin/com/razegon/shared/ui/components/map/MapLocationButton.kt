package com.razegon.shared.ui.components.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.razegon.shared.ui.theme.MaterialBlue700
import kotlinx.coroutines.launch
import org.maplibre.compose.location.LocationPermission

@Composable
fun MapLocationButton(
    permission: LocationPermission,
    snackbarHostState: SnackbarHostState,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    val canRequest = when (permission) {
        LocationPermission.NotDetermined -> true
        is LocationPermission.NotGranted -> permission.canRequest != false
        else -> false
    }

    if (permission !is LocationPermission.Granted) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                onClick = {
                    if (canRequest) {
                        onRequestPermission()
                    } else {
                        coroutineScope.launch {
                            val result = snackbarHostState.showSnackbar(
                                message = "Włącz lokalizację w ustawieniach aplikacji.",
                                actionLabel = "Ustawienia",
                                withDismissAction = true
                            )

                            if (result == SnackbarResult.ActionPerformed) {
                                onOpenSettings()
                            }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (canRequest) {
                        MaterialBlue700
                    } else {
                        Color.LightGray
                    },
                    contentColor = if (canRequest) {
                        Color.White
                    } else {
                        Color.DarkGray
                    }
                ),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 32.dp)
            ) {
                Text("Włącz lokalizację")
            }
        }
    }
}