package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BatchPerformanceBarChart
import com.example.ui.components.WeeklyCodingActivityLineChart
import com.example.ui.theme.*

@Composable
fun TeacherAnalyticsScreen(
    onNavigateBack: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back", tint = SleekIndigo)
                }
                Text("📈 Teacher Analytics & Weak Student Insights", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SleekTextPrimary)
            }
        }

        item {
            BatchPerformanceBarChart(
                batchData = listOf("B.Sc CS-A" to 88f, "B.Sc CS-B" to 76f, "BCA-A" to 82f, "BCA-B" to 68f)
            )
        }

        item {
            WeeklyCodingActivityLineChart()
        }

        // Weak Students Alert Section
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = Color(0xFFEF4444))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "⚠️ Weak-Student AI Diagnostic Report (14 Students)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF991B1B)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• Student 'K. Mahesh' has repeated errors in Pointers & Memory Allocation.\n• Student 'P. Swathi' has low compilation success rate (45%).\n• AI Recommendation: Assign Remedial Module 8 Pointer exercises.",
                        fontSize = 12.sp,
                        color = Color(0xFF7F1D1D),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626))
                    ) {
                        Text("Export Weak-Student Report (PDF/Excel)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

