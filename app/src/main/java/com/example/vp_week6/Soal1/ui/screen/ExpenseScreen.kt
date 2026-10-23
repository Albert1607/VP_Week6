package com.example.vp_week6.Soal1.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vp_week6.Soal1.model.Expense
import com.example.vp_week6.Soal1.viewmodel.ExpenseUiState
import com.example.vp_week6.Soal1.viewmodel.ExpenseViewModel
import java.text.NumberFormat
import java.util.Locale

fun formatRupiah(amount: Int): String {
    val localeID = Locale.forLanguageTag("id-ID")
    val formatter = NumberFormat.getNumberInstance(localeID)
    return "Rp ${formatter.format(amount)}"
}

@Composable
fun ExpenseScreen(
    viewModel: ExpenseViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ExpenseContent(
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onAmountChange = viewModel::onAmountChange,
        onAddExpense = viewModel::onAddExpense,
        onDeleteExpense = viewModel::onDeleteExpense,
        onStartEdit = viewModel::onStartEdit,
        onDismissEdit = viewModel::onDismissEdit,
        onSaveEdit = viewModel::onSaveEdit
    )
}

@Composable
fun ExpenseContent(
    uiState: ExpenseUiState,
    onNameChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onAddExpense: () -> Unit,
    onDeleteExpense: (String) -> Unit,
    onStartEdit: (Expense) -> Unit,
    onDismissEdit: () -> Unit,
    onSaveEdit: (String, String, Int) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Expenses",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Text(
                text = "Total Expense : ${formatRupiah(uiState.totalExpense)}",
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            OutlinedTextField(
                value = uiState.nameInput,
                onValueChange = onNameChange,
                label = { Text("Expense Name") },
                placeholder = { Text("Expense Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.amountInput,
                    onValueChange = onAmountChange,
                    label = { Text("Amount of Expense") },
                    placeholder = { Text("Amount of Expense") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = uiState.isAmountError,
                    supportingText = {
                        if (uiState.isAmountError) {
                            Text(
                                text = if (uiState.errorMessage.isNotEmpty()) uiState.errorMessage else "Amount must be greater than 0",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.weight(1f)
                )

                IconButton(
                    onClick = onAddExpense,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .size(44.dp)
                        .background(Color(0xFFE0E0E0), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Expense",
                        tint = Color.Gray
                    )
                }
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.expenses, key = { it.id }) { expense ->
                    ExpenseItemCard(
                        expense = expense,
                        onEdit = { onStartEdit(expense) },
                        onDelete = { onDeleteExpense(expense.id) }
                    )
                }
            }
        }

        FloatingActionButton(
            onClick = { },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            containerColor = Color.White,
            contentColor = Color.Black,
            shape = CircleShape
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu"
            )
        }

        if (uiState.editingExpense != null) {
            val expenseToEdit = uiState.editingExpense
            var editName by remember(expenseToEdit) { mutableStateOf(expenseToEdit.name) }
            var editAmount by remember(expenseToEdit) { mutableStateOf(expenseToEdit.amount.toString()) }
            var editError by remember(expenseToEdit) { mutableStateOf(false) }

            AlertDialog(
                onDismissRequest = onDismissEdit,
                title = { Text("Edit Expense") },
                text = {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = editName,
                            onValueChange = { editName = it },
                            label = { Text("Expense Name") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = editAmount,
                            onValueChange = {
                                editAmount = it
                                editError = false
                            },
                            label = { Text("Amount of Expense") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = editError,
                            supportingText = {
                                if (editError) {
                                    Text(
                                        text = "Amount must be greater than 0",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val amountInt = editAmount.toIntOrNull() ?: 0
                            if (amountInt <= 0) {
                                editError = true
                            } else {
                                onSaveEdit(expenseToEdit.id, editName, amountInt)
                            }
                        }
                    ) {
                        Text("Save")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = onDismissEdit) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ExpenseItemCard(
    expense: Expense,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE2E7FF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Name : ${expense.name}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF101828)
                )
                Text(
                    text = formatRupiah(expense.amount),
                    fontSize = 13.sp,
                    color = Color(0xFF475467)
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFFD4A017)
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color(0xFFD32F2F)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ExpenseScreenPreview() {
    ExpenseContent(
        uiState = ExpenseUiState(
            expenses = listOf(
                Expense(name = "Makan Nasi", amount = 90000),
                Expense(name = "Investasi", amount = 10000000),
                Expense(name = "Kopi", amount = 18000)
            )
        ),
        onNameChange = {},
        onAmountChange = {},
        onAddExpense = {},
        onDeleteExpense = {},
        onStartEdit = {},
        onDismissEdit = {},
        onSaveEdit = { _, _, _ -> }
    )
}
