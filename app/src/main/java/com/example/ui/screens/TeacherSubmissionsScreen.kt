package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.LabSubmission
import com.example.ui.theme.*

@Composable
fun TeacherSubmissionsScreen(
    submissions: List<LabSubmission>,
    language: AppLanguage,
    onEvaluateSubmission: (String, Int, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedSubmission by remember { mutableStateOf<LabSubmission?>(submissions.firstOrNull()) }
    var marksInput by remember { mutableStateOf("95") }
    var commentInput by remember { mutableStateOf("Excellent execution and algorithm logic!") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(onClick = onNavigateBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = SleekIndigo)
            }
            Text("📝 Student Lab Record Review & AI Grading", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SleekTextPrimary)
        }

        Spacer(modifier = Modifier.height(14.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(submissions) { sub ->
                val isSelected = selectedSubmission?.id == sub.id

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedSubmission = sub
                            marksInput = (sub.teacherMarks ?: sub.score).toString()
                            commentInput = sub.teacherComment ?: "Good C code structure."
                        },
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) SleekIndigoContainer else SleekSurface
                    ),
                    border = BorderStroke(1.dp, if (isSelected) SleekIndigo.copy(alpha = 0.4f) else SleekOutline)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(text = sub.studentName, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = SleekTextPrimary)
                                Text(text = "Roll: ${sub.studentRollNo} • ${sub.experimentId}", fontSize = 11.sp, color = SleekTextSecondary)
                            }
                            AssistChip(
                                onClick = {},
                                label = { Text("Score: ${sub.teacherMarks ?: sub.score}/100", fontWeight = FontWeight.Bold, color = SleekIndigo) },
                                colors = AssistChipDefaults.assistChipColors(containerColor = SleekSurface),
                                border = BorderStroke(1.dp, SleekOutline)
                            )
                        }

                        if (isSelected) {
                            Spacer(modifier = Modifier.height(14.dp))
                            Text("Submitted C Source Code:", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SleekTextPrimary)

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF090D16),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = sub.code,
                                    color = Color(0xFF86EFAC),
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text("AI Auto-Evaluation Feedback:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SleekIndigo)
                            Text(text = sub.aiFeedbackEn, fontSize = 11.sp, color = SleekTextSecondary)

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                OutlinedTextField(
                                    value = marksInput,
                                    onValueChange = { marksInput = it },
                                    label = { Text("Marks") },
                                    modifier = Modifier.width(90.dp),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                OutlinedTextField(
                                    value = commentInput,
                                    onValueChange = { commentInput = it },
                                    label = { Text("Teacher Comments") },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    val m = marksInput.toIntOrNull() ?: 90
                                    onEvaluateSubmission(sub.id, m, commentInput)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = SleekEmerald),
                                shape = RoundedCornerShape(14.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Approve & Save Faculty Marks", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}

