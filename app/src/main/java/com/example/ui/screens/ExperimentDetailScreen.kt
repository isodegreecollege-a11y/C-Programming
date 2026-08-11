package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import com.example.data.compiler.CInterpreter
import com.example.data.compiler.CompilationResult
import com.example.data.model.AppLanguage
import com.example.data.model.Experiment
import com.example.data.model.LabSubmission
import com.example.ui.components.CodeEditorView
import com.example.ui.components.FlowchartVisualizer
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun ExperimentDetailScreen(
    experiment: Experiment,
    language: AppLanguage,
    onSubmitRecord: (LabSubmission) -> Unit,
    onNavigateBack: () -> Unit
) {
    var code by remember { mutableStateOf(experiment.sampleCode) }
    var stdinInput by remember { mutableStateOf(experiment.sampleInput) }
    var compilationResult by remember { mutableStateOf<CompilationResult?>(null) }
    var isCompiling by remember { mutableStateOf(false) }
    var selectedTab by remember { mutableStateOf(0) } // 0 = Theory & Flowchart, 1 = Workspace & Test Cases, 2 = Viva Questions
    var testCaseResults by remember { mutableStateOf<List<Boolean>?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(14.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = SleekIndigo)
                }
                Column {
                    Text(
                        text = "Exp ${experiment.expNo}: ${if (language == AppLanguage.TELUGU) experiment.titleTe else experiment.titleEn}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SleekTextPrimary
                    )
                    Text(
                        text = if (language == AppLanguage.TELUGU) experiment.objectiveTe else experiment.objectiveEn,
                        fontSize = 11.sp,
                        color = SleekTextSecondary
                    )
                }
            }

            if (isSubmitted) {
                AssistChip(
                    onClick = {},
                    label = { Text("Submitted ✓", color = SleekEmerald, fontWeight = FontWeight.Bold) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = SleekEmeraldContainer),
                    border = BorderStroke(1.dp, SleekEmerald.copy(alpha = 0.3f))
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Navigation Tabs (Theory / Workspace / Viva)
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = SleekSurface,
            contentColor = SleekIndigo
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("📖 Theory & Flowchart", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("💻 Workspace & Test Cases", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { selectedTab = 2 },
                text = { Text("💬 Viva Voce", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> {
                // Tab 0: Theory, Algorithm, Flowchart, Telugu Explanation
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = SleekSurface),
                            border = BorderStroke(1.dp, SleekOutline)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text("🎯 Objective & Theory", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SleekTextPrimary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (language == AppLanguage.TELUGU) experiment.theoryTe else experiment.theoryEn,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp,
                                    color = SleekTextSecondary
                                )
                            }
                        }
                    }

                    item {
                        FlowchartVisualizer(steps = experiment.flowchartSteps)
                    }

                    item {
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = SleekIndigoContainer),
                            border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Text("🇮🇳 Telugu Explanation (తెలుగు వివరణ)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = SleekIndigo)
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = experiment.theoryTe,
                                    fontSize = 12.sp,
                                    color = SleekTextPrimary,
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }

            1 -> {
                // Tab 1: Code Workspace + Test Case Evaluation + Submission
                Column(modifier = Modifier.weight(1f)) {
                    CodeEditorView(
                        code = code,
                        onCodeChange = { code = it },
                        userInput = stdinInput,
                        onInputChange = { stdinInput = it },
                        output = compilationResult?.output ?: "",
                        errorMessage = compilationResult?.errorMessage ?: "",
                        isCompiling = isCompiling,
                        onRunCode = {
                            isCompiling = true
                            coroutineScope.launch {
                                compilationResult = CInterpreter.compileAndRun(code, stdinInput)
                                isCompiling = false
                            }
                        },
                        onFormatCode = { code = CInterpreter.formatCCode(code) },
                        onAskAiTutor = {},
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Test Cases Execution & Submission Bar
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SleekSurface),
                        border = BorderStroke(1.dp, SleekOutline)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🧪 Test Case Evaluation", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = SleekTextPrimary)

                                Button(
                                    onClick = {
                                        // Run automated test cases
                                        val results = experiment.testCases.map { tc ->
                                            val res = CInterpreter.compileAndRun(code, tc.input)
                                            res.isSuccess && res.output.trim().contains(tc.expectedOutput.trim())
                                        }
                                        testCaseResults = results
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SleekIndigo),
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                                ) {
                                    Text("Test All Cases", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            if (testCaseResults != null) {
                                Spacer(modifier = Modifier.height(8.dp))
                                val passedCount = testCaseResults!!.count { it }
                                val total = testCaseResults!!.size

                                Text(
                                    text = "Passed: $passedCount / $total Test Cases",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (passedCount == total) SleekEmerald else Color(0xFFEF4444)
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Button(
                                    onClick = {
                                        val sub = LabSubmission(
                                            id = "sub_${System.currentTimeMillis()}",
                                            experimentId = experiment.id,
                                            studentId = "std_101",
                                            studentName = "Sai Kumar Reddy",
                                            studentRollNo = "2024CS101",
                                            code = code,
                                            compilationStatus = if (compilationResult?.isSuccess == true) "SUCCESS" else "FAILED",
                                            testCasesPassed = passedCount,
                                            totalTestCases = total,
                                            score = if (passedCount == total) 95 else 60,
                                            aiFeedbackEn = "Code compiled smoothly with excellent formatting.",
                                            aiFeedbackTe = "కోడ్ విజయవంతంగా రన్ అయ్యింది."
                                        )
                                        onSubmitRecord(sub)
                                        isSubmitted = true
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = SleekEmerald),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("📤 Submit Lab Record to Faculty", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            2 -> {
                // Tab 2: Viva Voce Questions
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(experiment.vivaQuestions) { viva ->
                        Card(
                            shape = RoundedCornerShape(20.dp),
                            colors = CardDefaults.cardColors(containerColor = SleekSurface),
                            border = BorderStroke(1.dp, SleekOutline)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Q${viva.id}: ${if (language == AppLanguage.TELUGU) viva.questionTe else viva.questionEn}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekIndigo
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "Ans: ${if (language == AppLanguage.TELUGU) viva.answerTe else viva.answerEn}",
                                    fontSize = 12.sp,
                                    color = SleekTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

