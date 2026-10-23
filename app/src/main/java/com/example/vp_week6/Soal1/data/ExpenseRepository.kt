package com.example.vp_week6.Soal1.data

import com.example.vp_week6.Soal1.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object ExpenseRepository {
    private val _expenses = MutableStateFlow<List<Expense>>(
        listOf(
            Expense(name = "Makan Nasi", amount = 90000),
            Expense(name = "Investasi", amount = 10000000),
            Expense(name = "Kopi", amount = 18000)
        )
    )
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    fun add(expense: Expense) {
        _expenses.update { it + expense }
    }

    fun delete(id: String) {
        _expenses.update { list -> list.filterNot { it.id == id } }
    }

    fun update(id: String, name: String, amount: Int) {
        _expenses.update { list ->
            list.map { item ->
                if (item.id == id) {
                    item.copy(name = name, amount = amount)
                } else {
                    item
                }
            }
        }
    }
}
