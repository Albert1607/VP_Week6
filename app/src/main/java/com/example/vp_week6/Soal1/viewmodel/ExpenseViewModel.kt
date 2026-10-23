package com.example.vp_week6.Soal1.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vp_week6.Soal1.data.ExpenseRepository
import com.example.vp_week6.Soal1.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class ExpenseUiState(
    val expenses: List<Expense> = emptyList(),
    val nameInput: String = "",
    val amountInput: String = "",
    val showError: Boolean = false,
    val errorMessage: String = "",
    val editingExpense: Expense? = null
) {
    val totalExpense: Int get() = expenses.sumOf { it.amount }
    val isAmountError: Boolean get() = showError && (amountInput.isBlank() || (amountInput.toIntOrNull() ?: 0) <= 0)
}

class ExpenseViewModel(
    private val repository: ExpenseRepository = ExpenseRepository
) : ViewModel() {

    private val _inputState = MutableStateFlow(ExpenseUiState())

    val uiState: StateFlow<ExpenseUiState> = combine(
        repository.expenses,
        _inputState
    ) { expenseList, input ->
        input.copy(expenses = expenseList)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ExpenseUiState()
    )

    fun onNameChange(newName: String) {
        _inputState.update { it.copy(nameInput = newName) }
    }

    fun onAmountChange(newAmount: String) {
        _inputState.update {
            it.copy(
                amountInput = newAmount,
                showError = false,
                errorMessage = ""
            )
        }
    }

    fun onAddExpense() {
        val currentState = _inputState.value
        val name = currentState.nameInput.trim()
        val amount = currentState.amountInput.toIntOrNull() ?: 0

        if (amount <= 0) {
            _inputState.update {
                it.copy(
                    showError = true,
                    errorMessage = "Amount must be greater than 0"
                )
            }
            return
        }

        val finalName = if (name.isBlank()) "Expense" else name
        repository.add(Expense(name = finalName, amount = amount))

        _inputState.update {
            it.copy(
                nameInput = "",
                amountInput = "",
                showError = false,
                errorMessage = ""
            )
        }
    }

    fun onDeleteExpense(id: String) {
        repository.delete(id)
    }

    fun onStartEdit(expense: Expense) {
        _inputState.update { it.copy(editingExpense = expense) }
    }

    fun onDismissEdit() {
        _inputState.update { it.copy(editingExpense = null) }
    }

    fun onSaveEdit(id: String, newName: String, newAmount: Int) {
        if (newAmount > 0) {
            repository.update(id, newName.ifBlank { "Expense" }, newAmount)
            _inputState.update { it.copy(editingExpense = null) }
        }
    }
}
