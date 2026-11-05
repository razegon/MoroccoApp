package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.model.ArcadeGameState
import com.razegon.shared.domain.model.QuizQuestion
import com.razegon.shared.domain.repository.QuizRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

// Shared ViewModel responsible for Arcade game state, question flow and timer management.
class ArcadeViewModel(
    private val repository: QuizRepository,
    private val scope: CoroutineScope
) {

    // Stores all questions available during the current game.
    private var questions: List<QuizQuestion> = emptyList()

    // Exposes the current game state as a read-only StateFlow.
    private val _gameState = MutableStateFlow(ArcadeGameState())
    val gameState: StateFlow<ArcadeGameState> = _gameState

    // Starts the countdown timer, cancelling any previously running timer.
    private var timerJob: Job? = null

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = scope.launch {
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

    // Starts a new Arcade game by loading all available questions.
    suspend fun startGame() {
        questions = repository.getAllQuestions()

        if (questions.isEmpty()) {
            _gameState.value = ArcadeGameState(
                isGameOver = true
            )
            return
        }

        val firstQuestion = questions.random()

        _gameState.value = ArcadeGameState(
            currentQuestion = firstQuestion,
            usedQuestionIds = setOf(firstQuestion.id)
        )

        startTimer()

    }

    // Advances to a randomly selected question that has not been used yet.
    fun nextQuestion() {
        val state = _gameState.value

        if (state.isGameOver) {
            return
        }

        val availableQuestions = questions.filter {
            it.id !in state.usedQuestionIds
        }

        if (availableQuestions.isEmpty()) {
            _gameState.value = state.copy(
                isGameOver = true
            )
            return
        }

        val nextQuestion = availableQuestions.random()

        _gameState.value = state.copy(
            currentQuestion = nextQuestion,
            selectedAnswerIndex = null,
            isAnswerChecked = false,
            usedQuestionIds = state.usedQuestionIds + nextQuestion.id
        )
    }

    // Checks the selected answer and updates the score and remaining time.
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

        if (newTime == 0) {
            pauseTimer()
        }
    }

    // Stops the countdown timer.
    fun pauseTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    // Resumes the countdown timer if the game is still active.
    fun resumeTimer() {
        if (_gameState.value.isGameOver) {
            return
        }

        startTimer()
    }
}