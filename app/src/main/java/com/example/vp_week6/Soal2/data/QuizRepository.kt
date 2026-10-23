package com.example.vp_week6.Soal2.data

import com.example.vp_week6.Soal2.model.QuizQuestion

object QuizRepository {
    private val allQuestions = listOf(
        QuizQuestion(
            id = 1,
            questionText = "Ibu Kota Di Indonesia?",
            options = listOf("Surabaya", "Bandung", "Jakarta", "Medan"),
            correctAnswerIndex = 2
        ),
        QuizQuestion(
            id = 2,
            questionText = "Siapa Dosen Mata Kuliah VP?",
            options = listOf("Pak Rudi", "Bu Citra", "Pak Michael", "Pak Prabowo"),
            correctAnswerIndex = 2
        ),
        QuizQuestion(
            id = 3,
            questionText = "Natal dirayakan pada bulan?",
            options = listOf("Agustus", "Oktober", "November", "Desember"),
            correctAnswerIndex = 3
        ),
        QuizQuestion(
            id = 4,
            questionText = "Ada berapa hari dalam seminggu?",
            options = listOf("5", "6", "7", "8"),
            correctAnswerIndex = 2
        ),
        QuizQuestion(
            id = 5,
            questionText = "Mata Kuliah VP pakai apa?",
            options = listOf("Word", "Excel", "PowerPoint", "Android Studio"),
            correctAnswerIndex = 3
        )
    )

    fun getRandomQuestions(count: Int): List<QuizQuestion> {
        val safeCount = count.coerceIn(1, allQuestions.size)
        return allQuestions.shuffled().take(safeCount)
    }

    fun getTotalQuestionsCount(): Int = allQuestions.size
}
