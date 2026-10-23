package com.example.vp_week6.Soal2.model

data class QuizQuestion(
    val id: Int,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndex: Int
) {
    val correctAnswerText: String
        get() = options.getOrNull(correctAnswerIndex) ?: ""
}
