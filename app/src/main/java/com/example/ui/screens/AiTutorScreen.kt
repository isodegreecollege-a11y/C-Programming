package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.remote.GeminiRepository
import com.example.ui.theme.*
import kotlinx.coroutines.launch

data class ChatMessage(
    val sender: String, // "User" or "AI"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Composable
fun AiTutorScreen(
    initialCode: String = "",
    initialError: String = "",
    language: AppLanguage
) {
    var codeContext by remember { mutableStateOf(initialCode) }
    var userPrompt by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }

    val chatMessages = remember {
        mutableStateListOf(
            ChatMessage(
                sender = "AI",
                text = if (language == AppLanguage.TELUGU) {
                    "నమస్కారం! నేను మీ **AI C Lab ట్యూటర్**. మీకు C ప్రోగ్రామింగ్‌లో ఉన్న ఎర్రర్లు, సింటాక్స్ లేదా సందేహాలను అడగండి!"
                } else {
                    "Hello! I am your **AI C Lab Tutor**. Paste your C code or ask any questions about compilation errors, pointers, loops, or logic!"
                }
            )
        )
    }

    val coroutineScope = rememberCoroutineScope()

    fun sendQuery(prompt: String) {
        if (prompt.isBlank() && codeContext.isBlank()) return
        chatMessages.add(ChatMessage("User", prompt.ifBlank { "Explain current code" }))
        isThinking = true

        coroutineScope.launch {
            val reply = if (initialError.isNotBlank()) {
                GeminiRepository.analyzeAndFixError(codeContext, initialError, language)
            } else {
                GeminiRepository.askAiTutor(prompt, codeContext, language)
            }
            chatMessages.add(ChatMessage("AI", reply))
            isThinking = false
            userPrompt = ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp)
    ) {
        // Header Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SleekIndigoContainer),
            border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.2f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SleekIndigo),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = Icons.Default.Psychology, contentDescription = "AI", tint = Color.White)
                }

                Column {
                    Text(
                        text = if (language == AppLanguage.TELUGU) "AI C ప్రోగ్రామింగ్ ట్యూటర్" else "AI C Programming Tutor",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SleekIndigo
                    )
                    Text(
                        text = if (language == AppLanguage.TELUGU) "ద్విభాషా సహాయం (ఇంగ్లీష్ + తెలుగు)" else "Bilingual Assistance (English + Telugu)",
                        fontSize = 11.sp,
                        color = SleekTextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Preset Quick Action Chips
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            val chips = listOf(
                "Explain this code",
                "Find error & fix",
                "Explain in Telugu (తెలుగు వివరణ)",
                "Line by line explanation",
                "Create similar practice problem"
            )
            items(chips) { chip ->
                SuggestionChip(
                    onClick = { sendQuery(chip) },
                    label = { Text(chip, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = SleekIndigo) },
                    icon = { Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(14.dp), tint = SleekIndigo) },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.3f)),
                    colors = SuggestionChipDefaults.suggestionChipColors(containerColor = SleekSurface)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Chat Message Log
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(chatMessages) { msg ->
                val isUser = msg.sender == "User"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isUser) SleekIndigo else SleekSurface,
                        border = if (isUser) null else BorderStroke(1.dp, SleekOutline),
                        modifier = Modifier.widthIn(max = 320.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (isUser) Icons.Default.Person else Icons.Default.Psychology,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (isUser) Color.White else SleekIndigo
                                )
                                Text(
                                    text = if (isUser) "You" else "AI Tutor",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUser) Color.White else SleekIndigo
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = msg.text,
                                fontSize = 13.sp,
                                color = if (isUser) Color.White else SleekTextPrimary,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            if (isThinking) {
                item {
                    Row(
                        modifier = Modifier.padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp, color = SleekIndigo)
                        Text("AI Tutor is analyzing your program...", fontSize = 12.sp, color = SleekTextSecondary)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Input Prompt Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = userPrompt,
                onValueChange = { userPrompt = it },
                placeholder = {
                    Text(
                        if (language == AppLanguage.TELUGU) "ఉదా: scanf లో & ఎందుకు పాయింటర్ ఇస్తుంది?" else "Ask AI Tutor e.g., Why does scanf need &?",
                        fontSize = 12.sp,
                        color = SleekTextMuted
                    )
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SleekIndigo,
                    unfocusedBorderColor = SleekOutline,
                    focusedContainerColor = SleekSurface,
                    unfocusedContainerColor = SleekSurface
                )
            )

            FloatingActionButton(
                onClick = { sendQuery(userPrompt) },
                containerColor = SleekIndigo,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

