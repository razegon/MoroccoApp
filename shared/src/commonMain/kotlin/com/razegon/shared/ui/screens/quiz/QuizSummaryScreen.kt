package com.razegon.shared.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Shared quiz summary screen with result saving and navigation actions.
@Composable
fun QuizSummaryScreen(
    score: Int,
    questionCount: Int,
    isResultSaved: Boolean,
    onSaveResult: (String) -> Unit,
    onRankingClick: () -> Unit,
    onRestartClick: () -> Unit,
    onHomeClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nickname by rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "KONIEC QUIZU",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "$score / $questionCount",
            style = MaterialTheme.typography.displayMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = { newValue ->
                if (newValue.length <= 15) {
                    nickname = newValue
                }
            },
            enabled = !isResultSaved,
            label = { Text("Twój nick") },
            singleLine = true,
            maxLines = 1,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onSaveResult(nickname.trim()) },
            enabled = nickname.trim().isNotEmpty() && !isResultSaved,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                if (isResultSaved) {
                    "WYNIK ZAPISANY"
                } else {
                    "ZAPISZ WYNIK"
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onRankingClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("TOP 20")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onRestartClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("ZAGRAJ PONOWNIE")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onHomeClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("POWRÓT DO MENU")
        }
    }
}
