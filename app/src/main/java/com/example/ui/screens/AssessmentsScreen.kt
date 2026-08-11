package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.QuizModule
import com.example.ui.theme.*

@Composable
fun AssessmentsScreen(
    quizzes: List<QuizModule>,
    language: AppLanguage
) {
    var selectedQuiz by remember { mutableStateOf<QuizModule?>(quizzes.firstOrNull()) }
    val userAnswers = remember { mutableStateMapOf<Int, Int>() } // questionId -> selectedIndex
    var isSubmitted by remember { mutableStateOf(false) }

    val currentQuiz = selectedQuiz ?: return

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SleekIndigoContainer),
                border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier.padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = if (language == AppLanguage.TELUGU) currentQuiz.titleTe else currentQuiz.titleEn,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SleekIndigo
                        )
                        Text(
                            text = "${currentQuiz.questions.size} Questions • ${currentQuiz.timeLimitMinutes} Mins",
                            fontSize = 12.sp,
                            color = SleekTextSecondary
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Timer, contentDescription = null, tint = SleekAmber)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("10:00", fontWeight = FontWeight.Bold, color = SleekTextPrimary)
                    }
                }
            }
        }

        items(currentQuiz.questions.size) { idx ->
            val q = currentQuiz.questions[idx]
            val selectedOption = userAnswers[q.id]

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SleekSurface),
                border = BorderStroke(1.dp, SleekOutline)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Q${idx + 1}: ${if (language == AppLanguage.TELUGU) q.questionTe else q.questionEn}",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val options = if (language == AppLanguage.TELUGU) q.optionsTe else q.optionsEn
                    options.forEachIndexed { optIdx, optText ->
                        val isSelected = selectedOption == optIdx
                        val isCorrect = optIdx == q.correctIndex

                        val containerColor = when {
                            isSubmitted && isCorrect -> SleekEmerald
                            isSubmitted && isSelected && !isCorrect -> Color(0xFFEF4444)
                            isSelected -> SleekIndigo
                            else -> SleekSurfaceVariant
                        }

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = containerColor,
                            border = if (!isSelected && !isSubmitted) BorderStroke(1.dp, SleekOutline) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable(enabled = !isSubmitted) {
                                    userAnswers[q.id] = optIdx
                                }
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = optText,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected || (isSubmitted && isCorrect)) Color.White else SleekTextPrimary
                                )
                            }
                        }
                    }

                    if (isSubmitted) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "💡 ${if (language == AppLanguage.TELUGU) q.explanationTe else q.explanationEn}",
                            fontSize = 12.sp,
                            color = SleekIndigo,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        item {
            if (!isSubmitted) {
                Button(
                    onClick = { isSubmitted = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SleekIndigo)
                ) {
                    Text("Submit Assessment", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                val score = currentQuiz.questions.count { q -> userAnswers[q.id] == q.correctIndex }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SleekEmerald)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("🎉 Assessment Score: $score / ${currentQuiz.questions.size}", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        Text("Excellent performance! Badges updated.", fontSize = 12.sp, color = Color.White.copy(alpha = 0.9f))
                    }
                }
            }
        }
    }
}

