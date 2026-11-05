package com.razegon.moroccoapp.ui.screens.arcade

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.razegon.moroccoapp.viewmodel.ArcadeAndroidViewModel
import com.razegon.moroccoapp.viewmodel.ArcadeResultAndroidViewModel
import com.razegon.shared.domain.model.ArcadeResult
import com.razegon.shared.ui.screens.arcade.ArcadeGameScreen

// Android adapter connecting the shared arcade game UI with Android ViewModels.
@Composable
fun ArcadeScreen(
    viewModel: ArcadeAndroidViewModel,
    resultViewModel: ArcadeResultAndroidViewModel,
    onRankingClick: () -> Unit,
    onQuizMenuClick: () -> Unit,
    onExitClick: () -> Unit
) {
    val gameState by viewModel.gameState.collectAsState()
    var isResultSaved by rememberSaveable { mutableStateOf(false) }

    ArcadeGameScreen(
        gameState = gameState,
        isResultSaved = isResultSaved,
        onSelectAnswer = viewModel::selectAnswer,
        onNextQuestion = viewModel::nextQuestion,
        onSaveResult = { nickname, score ->
            val result = ArcadeResult(
                nickname = nickname.trim(),
                score = score,
                date = System.currentTimeMillis()
            )

            resultViewModel.saveResult(result)
            isResultSaved = true
        },
        onRankingClick = onRankingClick,
        onQuizMenuClick = onQuizMenuClick,
        onPauseTimer = viewModel::pauseTimer,
        onResumeTimer = viewModel::resumeTimer,
        onExitClick = onExitClick
    )
}
