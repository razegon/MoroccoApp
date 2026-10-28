
package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rezegon.shared.domain.model.ArcadeGameState
import com.rezegon.shared.domain.repository.QuizRepository
import com.rezegon.shared.presentation.viewmodel.ArcadeViewModel as SharedArcadeViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ArcadeAndroidViewModel(
    repository: QuizRepository
) : ViewModel() {

    private val sharedViewModel = SharedArcadeViewModel(
        repository = repository,
        scope = viewModelScope
    )

    val gameState: StateFlow<ArcadeGameState> = sharedViewModel.gameState

    fun startGame() {
        viewModelScope.launch {
            sharedViewModel.startGame()
        }
    }

    fun nextQuestion() = sharedViewModel.nextQuestion()

    fun selectAnswer(answerIndex: Int) =
        sharedViewModel.selectAnswer(answerIndex)

    fun pauseTimer() = sharedViewModel.pauseTimer()

    fun resumeTimer() = sharedViewModel.resumeTimer()
}


