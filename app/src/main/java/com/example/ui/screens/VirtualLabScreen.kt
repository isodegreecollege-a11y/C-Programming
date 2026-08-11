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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.CModule
import com.example.data.model.Experiment
import com.example.ui.theme.*

@Composable
fun VirtualLabScreen(
    modules: List<CModule>,
    language: AppLanguage,
    onOpenExperiment: (Experiment) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedModuleId by remember { mutableStateOf<String?>(null) }

    val filteredModules = remember(searchQuery, modules) {
        if (searchQuery.isBlank()) {
            modules
        } else {
            modules.filter { mod ->
                mod.titleEn.contains(searchQuery, ignoreCase = true) ||
                        mod.titleTe.contains(searchQuery, ignoreCase = true) ||
                        mod.experiments.any { exp ->
                            exp.titleEn.contains(searchQuery, ignoreCase = true) ||
                                    exp.titleTe.contains(searchQuery, ignoreCase = true)
                        }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp)
    ) {
        Text(
            text = if (language == AppLanguage.TELUGU) "వర్చువల్ C ప్రోగ్రామింగ్ ల్యాబ్ (10 మాడ్యూళ్ళు)" else "Virtual C Programming Lab (10 Modules)",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = SleekTextPrimary
        )
        Text(
            text = if (language == AppLanguage.TELUGU) "50+ ప్రయోగాత్మక కోడింగ్ ఎక్స్‌పెరిమెంట్స్" else "50+ Practical Coding Experiments & Test Cases",
            fontSize = 12.sp,
            color = SleekTextSecondary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Natural Language Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text(if (language == AppLanguage.TELUGU) "సెర్చ్: పాయింటర్లు, అర్రేలు, రికర్షన్..." else "Search e.g. arrays, pointers, recursion, loops...", fontSize = 12.sp, color = SleekTextMuted) },
            leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search", tint = SleekIndigo) },
            trailingIcon = {
                if (searchQuery.isNotBlank()) {
                    IconButton(onClick = { searchQuery = "" }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear", tint = SleekTextMuted)
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = SleekIndigo,
                unfocusedBorderColor = SleekOutline,
                focusedContainerColor = SleekSurface,
                unfocusedContainerColor = SleekSurface
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredModules) { module ->
                val isExpanded = selectedModuleId == module.id

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SleekSurface),
                    border = BorderStroke(1.dp, SleekOutline)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedModuleId = if (isExpanded) null else module.id
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SleekIndigoContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "M${module.moduleNo}",
                                        fontWeight = FontWeight.Bold,
                                        color = SleekIndigo,
                                        fontSize = 13.sp
                                    )
                                }

                                Column {
                                    Text(
                                        text = if (language == AppLanguage.TELUGU) module.titleTe else module.titleEn,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SleekTextPrimary
                                    )
                                    Text(
                                        text = "${module.experiments.size} Experiments Available",
                                        fontSize = 11.sp,
                                        color = SleekTextSecondary
                                    )
                                }
                            }

                            Icon(
                                imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = "Toggle",
                                tint = SleekIndigo
                            )
                        }

                        // Experiments List if expanded
                        if (isExpanded) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = SleekOutline)
                            Spacer(modifier = Modifier.height(12.dp))

                            module.experiments.forEach { exp ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(14.dp))
                                        .clickable { onOpenExperiment(exp) }
                                        .background(SleekSurfaceVariant)
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Science,
                                            contentDescription = null,
                                            tint = SleekIndigo,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Column {
                                            Text(
                                                text = "Exp ${exp.expNo}: ${if (language == AppLanguage.TELUGU) exp.titleTe else exp.titleEn}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = SleekTextPrimary
                                            )
                                            Text(
                                                text = if (language == AppLanguage.TELUGU) exp.objectiveTe else exp.objectiveEn,
                                                fontSize = 11.sp,
                                                color = SleekTextSecondary,
                                                maxLines = 1
                                            )
                                        }
                                    }

                                    Button(
                                        onClick = { onOpenExperiment(exp) },
                                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = SleekIndigo)
                                    ) {
                                        Text("Start", fontSize = 11.sp)
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

