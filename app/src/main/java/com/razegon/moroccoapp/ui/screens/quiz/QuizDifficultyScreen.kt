package com.razegon.moroccoapp.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.razegon.shared.domain.model.QuizDifficulty
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.TextButton
import androidx.compose.ui.unit.sp

@Composable
fun QuizDifficultyScreen(
    onDifficultySelected: (QuizDifficulty) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onBackClick
            ) {
                Text(
                    text = "←",
                    fontSize = 28.sp
                )
            }
        }

        Text(
            text = "WYBIERZ POZIOM",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Wybierz poziom trudności quizu.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )

        QuizDifficultyButton(
            text = "BARDZO ŁATWY",
            difficulty = QuizDifficulty.VERY_EASY,
            onDifficultySelected = onDifficultySelected
        )

        QuizDifficultyButton(
            text = "ŁATWY",
            difficulty = QuizDifficulty.EASY,
            onDifficultySelected = onDifficultySelected
        )

        QuizDifficultyButton(
            text = "ŚREDNI",
            difficulty = QuizDifficulty.MEDIUM,
            onDifficultySelected = onDifficultySelected
        )

        QuizDifficultyButton(
            text = "TRUDNY",
            difficulty = QuizDifficulty.HARD,
            onDifficultySelected = onDifficultySelected
        )

        QuizDifficultyButton(
            text = "BARDZO TRUDNY",
            difficulty = QuizDifficulty.VERY_HARD,
            onDifficultySelected = onDifficultySelected
        )
    }
}

@Composable
private fun QuizDifficultyButton(
    text: String,
    difficulty: QuizDifficulty,
    onDifficultySelected: (QuizDifficulty) -> Unit
) {
    OutlinedButton(
        onClick = {
            onDifficultySelected(difficulty)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(text)
    }
}