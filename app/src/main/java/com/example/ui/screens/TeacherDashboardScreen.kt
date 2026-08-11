package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.UserProfile
import com.example.ui.components.BatchPerformanceBarChart
import com.example.ui.theme.*

@Composable
fun TeacherDashboardScreen(
    user: UserProfile,
    language: AppLanguage,
    onNavigateSubmissions: () -> Unit,
    onNavigateAnalytics: () -> Unit
) {
    var selectedBatch by remember { mutableStateOf("B.Sc Computer Science - Sec A") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Teacher Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SleekIndigoContainer),
                border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                border = BorderStroke(1.5.dp, SleekIndigo),
                                color = Color.White,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.iso_logo),
                                    contentDescription = "ISO Logo",
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(3.dp)
                                        .clip(CircleShape)
                                )
                            }

                            Column {
                                Text(
                                    text = "👨‍🏫 Faculty Portal: ${user.name}",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = SleekIndigo
                                )
                                Text(
                                    text = "Department of Computer Science • ISO Degree College",
                                    fontSize = 11.sp,
                                    color = SleekTextSecondary
                                )
                                Text(
                                    text = "Designed by Makula Manohar",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekIndigo
                                )
                            }
                        }

                        AssistChip(
                            onClick = {},
                            label = { Text("Active Batch", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = SleekIndigo) },
                            colors = AssistChipDefaults.assistChipColors(containerColor = SleekSurface),
                            border = BorderStroke(1.dp, SleekOutline)
                        )
                    }
                }
            }
        }

        // Key Metric Grid Table
        item {
            Text("📊 C Programming Laboratory Metrics", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SleekTextPrimary)
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard("Total Students", "120", Icons.Default.Groups, SleekIndigo, Modifier.weight(1f))
                    MetricCard("Active Students", "108", Icons.Default.HowToReg, SleekEmerald, Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard("Labs Completed", "82%", Icons.Default.TaskAlt, SleekIndigo, Modifier.weight(1f))
                    MetricCard("Average Score", "76%", Icons.Default.Grade, SleekAmber, Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    MetricCard("Submissions", "1,240", Icons.Default.Folder, SleekIndigo, Modifier.weight(1f))
                    MetricCard("Weak Students", "14 Alerts", Icons.Default.Warning, Color(0xFFEF4444), Modifier.weight(1f))
                }
            }
        }

        // Quick Teacher Action Portals
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateSubmissions() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SleekEmeraldContainer),
                    border = BorderStroke(1.dp, SleekEmerald.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Icon(imageVector = Icons.Default.RateReview, contentDescription = null, tint = SleekEmerald)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Review Submissions", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SleekTextPrimary)
                        Text("12 Pending Reviews", fontSize = 11.sp, color = SleekTextSecondary)
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onNavigateAnalytics() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SleekIndigoContainer),
                    border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Icon(imageVector = Icons.Default.Analytics, contentDescription = null, tint = SleekIndigo)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Class Analytics", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = SleekTextPrimary)
                        Text("Charts & Weak Student Alerts", fontSize = 11.sp, color = SleekTextSecondary)
                    }
                }
            }
        }

        // Batch Performance Bar Chart
        item {
            BatchPerformanceBarChart(
                batchData = listOf("B.Sc CS-A" to 88f, "B.Sc CS-B" to 76f, "BCA-A" to 82f, "BCA-B" to 68f)
            )
        }
    }
}

@Composable
fun MetricCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = SleekSurface),
        border = BorderStroke(1.dp, SleekOutline)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = SleekTextPrimary)
                Text(text = title, fontSize = 10.sp, color = SleekTextSecondary)
            }
        }
    }
}

