package com.example.vp_week6.Soal2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.vp_week6.Soal2.data.QuizRepository
import com.example.vp_week6.Soal2.model.QuizQuestion
import com.example.vp_week6.Soal2.model.UserAnswer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class QuizScreenState {
    START,
    QUESTION,
    RESULT
}

data class QuizUiState(
    val screenState: QuizScreenState = QuizScreenState.START,
    val questionCountInput: String = "3",
    val isCountError: Boolean = false,
    val countErrorMessage: String = "",
    val activeQuestions: List<QuizQuestion> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val selectedOptionIndex: Int? = null,
    val userAnswers: List<UserAnswer> = emptyList()
) {
    val currentQuestion: QuizQuestion?
        get() = activeQuestions.getOrNull(currentQuestionIndex)

    val totalQuestions: Int
        get() = activeQuestions.size

    val score: Int
        get() = userAnswers.count { it.isCorrect }

    val wrongAnswers: List<UserAnswer>
        get() = userAnswers.filter { !it.isCorrect }
}

class QuizViewModel(
    private val repository: QuizRepository = QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    fun onQuestionCountInputChanged(input: String) {
        _uiState.update {
            it.copy(
                questionCountInput = input,
                isCountError = false,
                countErrorMessage = ""
            )
        }
    }

    fun onStartQuiz() {
        val inputVal = _uiState.value.questionCountInput.trim().toIntOrNull()
        val maxQuestions = repository.getTotalQuestionsCount()

        if (inputVal == null || inputVal <= 0 || inputVal > maxQuestions) {
            _uiState.update {
                it.copy(
                    isCountError = true,
                    countErrorMessage = "Maximum is $maxQuestions questions"
                )
            }
            return
        }

        val questions = repository.getRandomQuestions(inputVal)
        _uiState.update {
            it.copy(
                screenState = QuizScreenState.QUESTION,
                activeQuestions = questions,
                currentQuestionIndex = 0,
                selectedOptionIndex = null,
                userAnswers = emptyList(),
                isCountError = false,
                countErrorMessage = ""
            )
        }
    }

    fun onOptionSelected(index: Int) {
        _uiState.update { it.copy(selectedOptionIndex = index) }
    }

    fun onNextQuestion() {
        val state = _uiState.value
        val currentQ = state.currentQuestion ?: return
        val selectedOpt = state.selectedOptionIndex ?: return

        val answer = UserAnswer(
            question = currentQ,
            questionIndex = state.currentQuestionIndex + 1,
            selectedAnswerIndex = selectedOpt
        )

        val updatedAnswers = state.userAnswers + answer

        if (state.currentQuestionIndex + 1 < state.totalQuestions) {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = it.currentQuestionIndex + 1,
                    selectedOptionIndex = null,
                    userAnswers = updatedAnswers
                )
            }
        } else {
            _uiState.update {
                it.copy(
                    screenState = QuizScreenState.RESULT,
                    userAnswers = updatedAnswers
                )
            }
        }
    }

    fun onRestartQuiz() {
        _uiState.update {
            it.copy(
                screenState = QuizScreenState.START,
                questionCountInput = "3",
                isCountError = false,
                countErrorMessage = "",
                activeQuestions = emptyList(),
                currentQuestionIndex = 0,
                selectedOptionIndex = null,
                userAnswers = emptyList()
            )
        }
    }
}
