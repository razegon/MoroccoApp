package com.rezegon.moroccoapp.ui.screens.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.rezegon.moroccoapp.domain.model.QuizResult
import com.rezegon.moroccoapp.viewmodel.QuizResultViewModel

@Composable
fun QuizRankingScreen(
    resultViewModel: QuizResultViewModel,
    modifier: Modifier = Modifier
) {
    val results by resultViewModel.topResults.collectAsState()

    // Load the latest TOP 10 results when the ranking screen is opened.
    LaunchedEffect(Unit) {
        resultViewModel.loadTopResults()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "TOP 10",
            style = MaterialTheme.typography.headlineMedium
        )

        if (results.isEmpty()) {
            Text(
                text = "Brak zapisanych wyników",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(results) { index, result ->
                    QuizRankingItem(
                        position = index + 1,
                        result = result
                    )
                }
            }
        }
    }
}

@Composable
private fun QuizRankingItem(
    position: Int,
    result: QuizResult
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Text(
            text = "$position. ${result.nickname}    ${result.score}/${result.totalQuestions}    ${result.difficulty}",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp)
        )
    }
}