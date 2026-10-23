package com.example.vp_week6.Soal2.model

data class UserAnswer(
    val question: QuizQuestion,
    val questionIndex: Int,
    val selectedAnswerIndex: Int
) {
    val isCorrect: Boolean
        get() = selectedAnswerIndex == question.correctAnswerIndex

    val selectedAnswerText: String
        get() = question.options.getOrNull(selectedAnswerIndex) ?: ""
}
