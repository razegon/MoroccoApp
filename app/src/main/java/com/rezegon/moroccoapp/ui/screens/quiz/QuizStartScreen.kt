package com.rezegon.moroccoapp.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun QuizStartScreen(
    onStartQuizClick: () -> Unit,
    onArcadeClick: () -> Unit,
    onRankingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "QUIZ MAROKO",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "Sprawdź swoją wiedzę o Maroku.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "10 PYTAŃ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "5 poziomów trudności",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        Button(
            onClick = onStartQuizClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ROZPOCZNIJ QUIZ")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onArcadeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ARCADE MODE")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        OutlinedButton(
            onClick = onRankingClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("TOP 10")
        }
    }
}