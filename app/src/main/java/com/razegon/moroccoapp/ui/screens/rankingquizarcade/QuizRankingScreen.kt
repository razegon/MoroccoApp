package com.razegon.moroccoapp.ui.screens.rankingquizarcade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.razegon.moroccoapp.viewmodel.QuizResultAndroidViewModel
import com.razegon.shared.ui.screens.quiz.QuizRankingContent
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun QuizRankingScreen(
    resultViewModel: QuizResultAndroidViewModel,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val results by resultViewModel.topResults.collectAsState()

    // Load the latest quiz results when the ranking screen is opened.
    LaunchedEffect(Unit) {
        resultViewModel.loadTopResults()
    }

    QuizRankingContent(
        results = results,
        onBackClick = onBackClick,
        formatResultDate = { timestamp ->
            SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                .format(Date(timestamp))
        },
        modifier = modifier
    )
}

