package com.example.vp_week6.Soal2.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.vp_week6.Soal2.model.QuizQuestion
import com.example.vp_week6.Soal2.model.UserAnswer
import com.example.vp_week6.Soal2.viewmodel.QuizScreenState
import com.example.vp_week6.Soal2.viewmodel.QuizUiState
import com.example.vp_week6.Soal2.viewmodel.QuizViewModel

private val BrandPrimary = Color(0xFF4C5D8A)
private val OptionUnselectedBg = Color(0xFFE2E7FF)
private val OptionUnselectedText = Color(0xFF101828)

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    QuizContent(
        uiState = uiState,
        onQuestionCountInputChanged = viewModel::onQuestionCountInputChanged,
        onStartQuiz = viewModel::onStartQuiz,
        onOptionSelected = viewModel::onOptionSelected,
        onNextQuestion = viewModel::onNextQuestion,
        onRestartQuiz = viewModel::onRestartQuiz
    )
}

@Composable
fun QuizContent(
    uiState: QuizUiState,
    onQuestionCountInputChanged: (String) -> Unit,
    onStartQuiz: () -> Unit,
    onOptionSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit,
    onRestartQuiz: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FE))
    ) {
        when (uiState.screenState) {
            QuizScreenState.START -> {
                StartContent(
                    uiState = uiState,
                    onQuestionCountInputChanged = onQuestionCountInputChanged,
                    onStartQuiz = onStartQuiz
                )
            }
            QuizScreenState.QUESTION -> {
                QuestionContent(
                    uiState = uiState,
                    onOptionSelected = onOptionSelected,
                    onNextQuestion = onNextQuestion
                )
            }
            QuizScreenState.RESULT -> {
                ResultContent(
                    uiState = uiState,
                    onRestartQuiz = onRestartQuiz
                )
            }
        }
    }
}

@Composable
fun StartContent(
    uiState: QuizUiState,
    onQuestionCountInputChanged: (String) -> Unit,
    onStartQuiz: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Mini Quiz",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF101828)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "How many questions do you want? (1 - 5)",
                fontSize = 14.sp,
                color = Color(0xFF667085)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = uiState.questionCountInput,
                onValueChange = onQuestionCountInputChanged,
                label = { Text("Number of questions") },
                placeholder = { Text("Number of questions") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = uiState.isCountError,
                supportingText = {
                    if (uiState.isCountError) {
                        Text(
                            text = uiState.countErrorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onStartQuiz,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(text = "Start Quiz", fontSize = 14.sp)
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
    }
}

@Composable
fun QuestionContent(
    uiState: QuizUiState,
    onOptionSelected: (Int) -> Unit,
    onNextQuestion: () -> Unit
) {
    val currentQuestion = uiState.currentQuestion ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Question ${uiState.currentQuestionIndex + 1}",
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF101828)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = currentQuestion.questionText,
            fontSize = 14.sp,
            color = Color(0xFF667085),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            currentQuestion.options.forEachIndexed { index, optionText ->
                val isSelected = uiState.selectedOptionIndex == index
                val bgColor = if (isSelected) BrandPrimary else OptionUnselectedBg
                val textColor = if (isSelected) Color.White else OptionUnselectedText

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = bgColor,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .clickable { onOptionSelected(index) }
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = optionText,
                            color = textColor,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onNextQuestion,
            enabled = uiState.selectedOptionIndex != null,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = BrandPrimary,
                disabledContainerColor = Color(0xFFE2E7FF),
                contentColor = Color.White,
                disabledContentColor = Color.Gray
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
        ) {
            Text(text = "Next", fontSize = 14.sp)
        }
    }
}

@Composable
fun ResultContent(
    uiState: QuizUiState,
    onRestartQuiz: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Quiz Finished!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF101828)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Your Score: ${uiState.score} / ${uiState.totalQuestions}",
            fontSize = 16.sp,
            color = Color(0xFF475467)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (uiState.wrongAnswers.isEmpty()) {
            Text(
                text = "Perfect! No wrong answers.",
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = BrandPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = false),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.wrongAnswers) { wrongAnswer ->
                    WrongAnswerItem(wrongAnswer = wrongAnswer)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = onRestartQuiz,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp)
        ) {
            Text(text = "Restart Quiz", fontSize = 14.sp)
        }
    }
}

@Composable
fun WrongAnswerItem(wrongAnswer: UserAnswer) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = "Q${wrongAnswer.questionIndex}: ${wrongAnswer.question.questionText}",
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF101828)
        )
        Text(
            text = "Your Answer: ${wrongAnswer.selectedAnswerText}",
            fontSize = 12.sp,
            color = Color(0xFFD32F2F)
        )
        Text(
            text = "Correct Answer: ${wrongAnswer.question.correctAnswerText}",
            fontSize = 12.sp,
            color = BrandPrimary
        )
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            color = Color(0xFFE4E7EC)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StartContentPreview() {
    QuizContent(
        uiState = QuizUiState(screenState = QuizScreenState.START),
        onQuestionCountInputChanged = {},
        onStartQuiz = {},
        onOptionSelected = {},
        onNextQuestion = {},
        onRestartQuiz = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun QuestionContentPreview() {
    QuizContent(
        uiState = QuizUiState(
            screenState = QuizScreenState.QUESTION,
            activeQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    questionText = "Ibu Kota Di Indonesia?",
                    options = listOf("Surabaya", "Bandung", "Jakarta", "Medan"),
                    correctAnswerIndex = 2
                )
            ),
            selectedOptionIndex = 2
        ),
        onQuestionCountInputChanged = {},
        onStartQuiz = {},
        onOptionSelected = {},
        onNextQuestion = {},
        onRestartQuiz = {}
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ResultContentPreview() {
    QuizContent(
        uiState = QuizUiState(
            screenState = QuizScreenState.RESULT,
            activeQuestions = listOf(
                QuizQuestion(
                    id = 1,
                    questionText = "Ibu Kota Di Indonesia?",
                    options = listOf("Surabaya", "Bandung", "Jakarta", "Medan"),
                    correctAnswerIndex = 2
                )
            ),
            userAnswers = listOf(
                UserAnswer(
                    question = QuizQuestion(
                        id = 1,
                        questionText = "Ibu Kota Di Indonesia?",
                        options = listOf("Surabaya", "Bandung", "Jakarta", "Medan"),
                        correctAnswerIndex = 2
                    ),
                    questionIndex = 1,
                    selectedAnswerIndex = 0
                )
            )
        ),
        onQuestionCountInputChanged = {},
        onStartQuiz = {},
        onOptionSelected = {},
        onNextQuestion = {},
        onRestartQuiz = {}
    )
}
