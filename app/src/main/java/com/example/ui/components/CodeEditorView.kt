package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.compiler.CInterpreter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeEditorView(
    code: String,
    onCodeChange: (String) -> Unit,
    userInput: String,
    onInputChange: (String) -> Unit,
    output: String,
    errorMessage: String,
    isCompiling: Boolean,
    onRunCode: () -> Unit,
    onFormatCode: () -> Unit,
    onAskAiTutor: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableStateOf(0) } // 0 = Output, 1 = Input
    val lineCount = remember(code) { maxOf(1, code.lines().size) }
    val lineNumbersString = remember(lineCount) { (1..lineCount).joinToString("\n") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .background(Color(0xFF0F172A))
    ) {
        // Editor Header Toolbar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1E293B))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(Color(0xFFEF4444)))
                Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(Color(0xFFF59E0B)))
                Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(5.dp)).background(Color(0xFF10B981)))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "main.c",
                    color = Color(0xFF94A3B8),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                IconButton(
                    onClick = onFormatCode,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoFixHigh,
                        contentDescription = "Format Code",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(18.dp)
                    )
                }

                IconButton(
                    onClick = onAskAiTutor,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "Ask AI",
                        tint = Color(0xFFA855F7),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Button(
                    onClick = onRunCode,
                    enabled = !isCompiling,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF10B981),
                        contentColor = Color.White
                    ),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    if (isCompiling) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Run",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Run", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Code Area with Line Numbers
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color(0xFF090D16))
        ) {
            // Line Numbers Column
            val scrollState = rememberScrollState()
            Column(
                modifier = Modifier
                    .width(42.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF0F172A))
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = lineNumbersString,
                    color = Color(0xFF475569),
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
            }

            // Editable C Source Code
            OutlinedTextField(
                value = code,
                onValueChange = onCodeChange,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                textStyle = TextStyle(
                    color = Color(0xFFE2E8F0),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 18.sp
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color(0xFF090D16),
                    unfocusedContainerColor = Color(0xFF090D16)
                )
            )
        }

        // Console & Terminal Output Tabs
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(Color(0xFF1E293B))
        ) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color(0xFF0F172A),
                contentColor = Color(0xFF38BDF8)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Terminal,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = if (errorMessage.isNotBlank()) Color(0xFFEF4444) else Color(0xFF10B981)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Output Console", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Input,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Standard Input (stdin)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                if (selectedTab == 0) {
                    val consoleText = if (errorMessage.isNotBlank()) {
                        "❌ COMPILATION / RUNTIME ERROR:\n$errorMessage"
                    } else if (output.isNotBlank()) {
                        "▶ OUTPUT:\n$output"
                    } else {
                        "Press 'Run' to compile and execute program output..."
                    }

                    Text(
                        text = consoleText,
                        color = if (errorMessage.isNotBlank()) Color(0xFFFCA5A5) else Color(0xFF86EFAC),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    )
                } else {
                    OutlinedTextField(
                        value = userInput,
                        onValueChange = onInputChange,
                        placeholder = {
                            Text(
                                "Enter stdin input values separated by newlines (for scanf)...",
                                color = Color(0xFF64748B),
                                fontSize = 11.sp
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                        textStyle = TextStyle(
                            color = Color(0xFFE2E8F0),
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF38BDF8),
                            unfocusedBorderColor = Color(0xFF334155),
                            focusedContainerColor = Color(0xFF0F172A),
                            unfocusedContainerColor = Color(0xFF0F172A)
                        )
                    )
                }
            }
        }
    }
}
