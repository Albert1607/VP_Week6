package com.example.vp_week6.Soal1.model

import java.util.UUID

data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val amount: Int
)
