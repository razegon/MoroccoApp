
package com.rezegon.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.rezegon.shared.ui.screens.home.HomeScreen

private enum class AppScreen {
    Home,
    Places
}

@Composable
fun MoroccoApp() {
    var currentScreen by remember {
        mutableStateOf(AppScreen.Home)
    }

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Scaffold { innerPadding ->
                when (currentScreen) {
                    AppScreen.Home -> HomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        onCitiesClick = {
                            // Miasta dodamy w kolejnym etapie.
                        },
                        onPlacesClick = {
                            currentScreen = AppScreen.Places
                        },
                        onQuizClick = {
                            // Quiz dodamy później.
                        }
                    )

                    AppScreen.Places -> PlacesScreen(
                        modifier = Modifier.padding(innerPadding),
                        onBack = {
                            currentScreen = AppScreen.Home
                        }
                    )
                }
            }
        }
    }
}


@Composable
private fun PlacesScreen(
    modifier: Modifier = Modifier,
    onBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        TextButton(onClick = onBack) {
            Text("← Powrót")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Miejsca Maroka",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "W następnym kroku podłączymy tutaj dane z PlacesRepository.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


@Preview(
    name = "MoroccoApp - Home",
    showBackground = true,
    widthDp = 393,
    heightDp = 852,

)
@Composable
private fun MoroccoHomePreview() {
    MaterialTheme {
        MoroccoApp()
    }

}


