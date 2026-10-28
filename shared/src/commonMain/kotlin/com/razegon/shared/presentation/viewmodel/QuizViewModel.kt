
package com.razegon.shared.presentation.viewmodel

import com.razegon.shared.domain.model.QuizDifficulty
import com.razegon.shared.domain.model.QuizQuestion
import com.razegon.shared.domain.repository.QuizRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class QuizViewModel(
    private val repository: QuizRepository,
    private val scope: CoroutineScope
) {
    private var questions: List<QuizQuestion> = emptyList()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _selectedAnswerIndex = MutableStateFlow<Int?>(null)
    val selectedAnswerIndex: StateFlow<Int?> = _selectedAnswerIndex.asStateFlow()

    private val _score = MutableStateFlow(0)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _selectedDifficulty = MutableStateFlow<QuizDifficulty?>(null)
    val selectedDifficulty: StateFlow<QuizDifficulty?> = _selectedDifficulty.asStateFlow()

    private val _isAnswerChecked = MutableStateFlow(false)
    val isAnswerChecked: StateFlow<Boolean> = _isAnswerChecked.asStateFlow()

    private val _isResultSaved = MutableStateFlow(false)
    val isResultSaved: StateFlow<Boolean> = _isResultSaved.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    val currentQuestion: QuizQuestion?
        get() = questions.getOrNull(_currentQuestionIndex.value)

    val questionCount: Int
        get() = questions.size

    fun startQuiz(difficulty: QuizDifficulty) {
        scope.launch {
            _isLoading.value = true
            _error.value = null
            _selectedDifficulty.value = difficulty
            questions = emptyList()
            _currentQuestionIndex.value = 0
            _selectedAnswerIndex.value = null
            _score.value = 0
            _isAnswerChecked.value = false
            _isResultSaved.value = false

            try {
                questions = repository.getQuestions(difficulty).shuffled()

                if (questions.isEmpty()) {
                    _error.value = "Brak pytań dla wybranego poziomu trudności."
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _error.value = e.message ?: "Nie udało się wczytać pytań."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectAnswer(answerIndex: Int) {
        if (_isAnswerChecked.value) return

        val question = currentQuestion ?: return
        if (answerIndex !in question.answers.indices) return

        _selectedAnswerIndex.value = answerIndex
        _isAnswerChecked.value = true

        if (answerIndex == question.correctAnswerIndex) {
            _score.value += 1
        }
    }

    fun nextQuestion(): Boolean {
        if (!_isAnswerChecked.value) return false

        return if (_currentQuestionIndex.value < questions.lastIndex) {
            _currentQuestionIndex.value += 1
            _selectedAnswerIndex.value = null
            _isAnswerChecked.value = false
            true
        } else {
            false
        }
    }

    fun restartQuiz() {
        questions = emptyList()
        _currentQuestionIndex.value = 0
        _selectedAnswerIndex.value = null
        _score.value = 0
        _isAnswerChecked.value = false
        _isResultSaved.value = false
        _selectedDifficulty.value = null
        _isLoading.value = false
        _error.value = null
    }

    fun markResultAsSaved() {
        _isResultSaved.value = true
    }
}
