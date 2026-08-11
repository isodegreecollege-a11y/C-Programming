package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.compiler.CInterpreter
import com.example.data.compiler.CompilationResult
import com.example.data.model.AppLanguage
import com.example.data.model.ProgramRecord
import com.example.ui.components.CodeEditorView
import com.example.ui.theme.*
import kotlinx.coroutines.launch

@Composable
fun CCompilerScreen(
    language: AppLanguage,
    onSaveProgram: (ProgramRecord) -> Unit,
    onNavigateAiTutorWithCode: (String, String) -> Unit
) {
    var code by remember {
        mutableStateOf(
            """
            #include <stdio.h>
            
            int main() {
                printf("Hello World! Welcome to AI C Lab.\n");
                int a = 10, b = 20;
                printf("Sum of %d + %d = %d\n", a, b, a + b);
                return 0;
            }
            """.trimIndent()
        )
    }

    var userInput by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<CompilationResult?>(null) }
    var isCompiling by remember { mutableStateOf(false) }
    var programTitle by remember { mutableStateOf("My C Program") }
    var showSaveDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val codeTemplates = listOf(
        "Hello World" to """
            #include <stdio.h>
            
            int main() {
                printf("Hello, AI C Lab!\n");
                return 0;
            }
        """.trimIndent(),
        "Factorial (for)" to """
            #include <stdio.h>
            
            int main() {
                int n = 5, fact = 1;
                for(int i = 1; i <= n; i++) fact *= i;
                printf("Factorial of %d = %d\n", n, fact);
                return 0;
            }
        """.trimIndent(),
        "Pointers Swap" to """
            #include <stdio.h>
            
            void swap(int *x, int *y) {
                int temp = *x;
                *x = *y;
                *y = temp;
            }
            
            int main() {
                int a = 5, b = 15;
                swap(&a, &b);
                printf("Swapped: a = %d, b = %d\n", a, b);
                return 0;
            }
        """.trimIndent(),
        "Structure" to """
            #include <stdio.h>
            
            struct Student {
                int rollNo;
                char name[20];
            };
            
            int main() {
                struct Student s = {101, "Sai"};
                printf("Roll: %d, Name: %s\n", s.rollNo, s.name);
                return 0;
            }
        """.trimIndent()
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp)
    ) {
        // Top Toolbar Actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (language == AppLanguage.TELUGU) "ఆన్‌లైన్ C కంపైలర్" else "Online C Compiler",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = SleekTextPrimary
                )
                Text(
                    text = if (result != null) "Exec Time: ${result?.executionTimeMs}ms • Memory: ${result?.memoryUsageKb}KB" else "Isolated Sandbox Compiler",
                    fontSize = 11.sp,
                    color = SleekTextSecondary
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = { showSaveDialog = true },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, SleekOutline),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SleekIndigo)
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = "Save", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Save", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                Button(
                    onClick = {
                        onNavigateAiTutorWithCode(code, result?.errorMessage ?: "")
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SleekIndigo),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(imageVector = Icons.Default.Psychology, contentDescription = "Ask AI", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Ask AI", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Preset Templates Carousel
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(codeTemplates) { (name, snippet) ->
                SuggestionChip(
                    onClick = {
                        code = snippet
                        result = null
                    },
                    label = { Text(name, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = SleekIndigo) },
                    icon = { Icon(imageVector = Icons.Default.Code, contentDescription = null, modifier = Modifier.size(14.dp), tint = SleekIndigo) },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.3f)),
                    colors = SuggestionChipDefaults.suggestionChipColors(containerColor = SleekSurface)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Interactive Editor Component
        CodeEditorView(
            code = code,
            onCodeChange = { code = it },
            userInput = userInput,
            onInputChange = { userInput = it },
            output = result?.output ?: "",
            errorMessage = result?.errorMessage ?: "",
            isCompiling = isCompiling,
            onRunCode = {
                isCompiling = true
                coroutineScope.launch {
                    val comp = CInterpreter.compileAndRun(code, userInput)
                    result = comp
                    isCompiling = false
                }
            },
            onFormatCode = {
                code = CInterpreter.formatCCode(code)
            },
            onAskAiTutor = {
                onNavigateAiTutorWithCode(code, result?.errorMessage ?: "")
            },
            modifier = Modifier.weight(1f)
        )
    }

    // Save Program Dialog
    if (showSaveDialog) {
        AlertDialog(
            onDismissRequest = { showSaveDialog = false },
            containerColor = SleekSurface,
            title = { Text("Save Program to History", color = SleekTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                OutlinedTextField(
                    value = programTitle,
                    onValueChange = { programTitle = it },
                    label = { Text("Program Title") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            },
            confirmButton = {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = SleekIndigo),
                    onClick = {
                        onSaveProgram(
                            ProgramRecord(
                                userId = "std_101",
                                title = programTitle,
                                code = code,
                                input = userInput,
                                output = result?.output ?: "",
                                executionTimeMs = result?.executionTimeMs ?: 12,
                                hasError = result?.isSuccess == false
                            )
                        )
                        showSaveDialog = false
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showSaveDialog = false }) {
                    Text("Cancel", color = SleekTextSecondary)
                }
            }
        )
    }
}

