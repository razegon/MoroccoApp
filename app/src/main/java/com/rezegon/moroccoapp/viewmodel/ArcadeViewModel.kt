package com.rezegon.moroccoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.rezegon.moroccoapp.domain.model.ArcadeGameState
import com.rezegon.moroccoapp.domain.model.QuizQuestion
import com.rezegon.moroccoapp.domain.repository.QuizRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope

class ArcadeViewModel(
    private val repository: QuizRepository
) : ViewModel() {

    // Holds the complete pool of questions used during the current Arcade game.
    private var questions: List<QuizQuestion> = emptyList()

    // Exposes the current Arcade state to the UI while keeping mutations inside the ViewModel.
    private val _gameState = MutableStateFlow(ArcadeGameState())
    val gameState: StateFlow<ArcadeGameState> = _gameState

    private var timerJob: Job? = null

    private fun startTimer() {

        timerJob?.cancel()

        timerJob = viewModelScope.launch {

            while (isActive) {

                delay(1000)

                val state = _gameState.value

                if (state.isGameOver) {
                    break
                }

                val newTime = state.remainingTimeSeconds - 1

                if (newTime <= 0) {

                    _gameState.value = state.copy(
                        remainingTimeSeconds = 0,
                        isGameOver = true
                    )

                    break

                } else {

                    _gameState.value = state.copy(
                        remainingTimeSeconds = newTime
                    )
                }
            }
        }
    }

    // Starts a new Arcade game using all available questions.
    fun startGame() {
        questions = repository.getAllQuestions()

        val firstQuestion = questions.random()

        _gameState.value = ArcadeGameState(
            currentQuestion = firstQuestion,
            usedQuestionIds = setOf(firstQuestion.id)
        )

        startTimer()

    }

    fun nextQuestion() {
        val state = _gameState.value

        if (state.isGameOver) {
            return
        }

        val availableQuestions = questions.filter {
            it.id !in state.usedQuestionIds
        }

        val nextQuestion = availableQuestions.random()

        _gameState.value = state.copy(
            currentQuestion = nextQuestion,
            selectedAnswerIndex = null,
            isAnswerChecked = false,
            usedQuestionIds = state.usedQuestionIds + nextQuestion.id
        )
    }

    fun selectAnswer(answerIndex: Int) {

        val state = _gameState.value

        // Ignore input after an answer was already checked or when the game is over.
        if (state.isAnswerChecked || state.isGameOver) {
            return
        }

        val question = state.currentQuestion ?: return

        val isCorrect = answerIndex == question.correctAnswerIndex

        val newScore = if (isCorrect) {
            state.score + 1
        } else {
            state.score
        }

        val newTime = if (isCorrect) {
            state.remainingTimeSeconds + 5
        } else {
            maxOf(0, state.remainingTimeSeconds - 5)
        }

        _gameState.value = state.copy(
            selectedAnswerIndex = answerIndex,
            isAnswerChecked = true,
            score = newScore,
            remainingTimeSeconds = newTime,
            isGameOver = newTime == 0
        )
    }

    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resumeTimer() {
        if (_gameState.value.isGameOver) {
            return
        }

        startTimer()
    }
}