package com.rezegon.moroccoapp.ui.screens.arcade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rezegon.moroccoapp.domain.model.ArcadeResult
import com.rezegon.moroccoapp.viewmodel.ArcadeResultViewModel
import com.rezegon.moroccoapp.viewmodel.ArcadeViewModel


@Composable
fun ArcadeScreen(
    viewModel: ArcadeViewModel,
    resultViewModel: ArcadeResultViewModel,
    onRankingClick: () -> Unit,
    onQuizMenuClick: () -> Unit,
    onExitClick: () -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()

    var nickname by rememberSaveable {
        mutableStateOf("")
    }

    var isResultSaved by rememberSaveable {
        mutableStateOf(false)
    }

    var showExitDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val question = gameState.currentQuestion

    if (question == null) {
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(
                    onClick = {
                        viewModel.pauseTimer()
                        showExitDialog = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Wyjdź z gry"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${gameState.remainingTimeSeconds}s",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Text(
            text = "Wynik: ${gameState.score}"
        )

        Text(
            text = question.question
        )

        question.answers.forEachIndexed { index, answer ->

            val isSelected = gameState.selectedAnswerIndex == index
            val isCorrect = index == question.correctAnswerIndex

            val containerColor = when {
                gameState.isAnswerChecked && isCorrect ->
                    Color(0xFF4CAF50)

                gameState.isAnswerChecked && isSelected && !isCorrect ->
                    Color(0xFFE53935)

                else ->
                    Color.Unspecified
            }

            Button(
                onClick = {
                    viewModel.selectAnswer(index)
                },
                enabled = !gameState.isAnswerChecked && !gameState.isGameOver,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = containerColor,
                    disabledContainerColor = containerColor
                )
            ) {
                Text(answer)
            }
        }

        if (!gameState.isGameOver && gameState.isAnswerChecked) {

            Button(
                onClick = {
                    viewModel.nextQuestion()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("NASTĘPNE")
            }
        }

        if (gameState.isGameOver) {

            Text(
                text = "KONIEC GRY",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Wynik: ${gameState.score}",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = nickname,
                onValueChange = {
                    if (it.length <= 15) {
                        nickname = it
                    }
                },
                label = {
                    Text("Nick")
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done
                ),
                enabled = !isResultSaved
            )

            Button(
                onClick = {

                    val result = ArcadeResult(
                        nickname = nickname.trim(),
                        score = gameState.score,
                        date = System.currentTimeMillis()
                    )

                    resultViewModel.saveResult(result)
                    isResultSaved = true
                },
                enabled = nickname.trim().isNotEmpty() && !isResultSaved,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isResultSaved) {
                        "WYNIK ZAPISANY"
                    } else {
                        "ZAPISZ WYNIK"
                    }
                )
            }

            if (isResultSaved) {

                Button(
                    onClick = onRankingClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("TOP 10 ARCADE")
                }

                Button(
                    onClick = onQuizMenuClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("MENU QUIZU")
                }
            }
        }
    }

    // Confirms that leaving the Arcade game is intentional before discarding progress.
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.resumeTimer()
                showExitDialog = false
            },
            title = {
                Text("Czy chcesz zakończyć Arcade?")
            },
            text = {
                Text("Twój dotychczasowy wynik zostanie utracony.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        onExitClick()
                    }
                ) {
                    Text("Wyjdź")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.resumeTimer()
                        showExitDialog = false
                    }
                ) {
                    Text("Zostań")
                }
            }
        )
    }
}