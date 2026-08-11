package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@Composable
fun BatchPerformanceBarChart(
    batchData: List<Pair<String, Float>>, // e.g., ("BCA-A", 85f), ("BCA-B", 72f)
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SleekSurface),
        border = BorderStroke(1.dp, SleekOutline)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "📊 Batch-Wise Lab Completion Rate (%)",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SleekTextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))

            val barColor = SleekIndigo

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val barWidth = canvasWidth / (batchData.size * 2)

                val padding30 = 30.dp.toPx()
                val padding20 = 20.dp.toPx()

                batchData.forEachIndexed { index, (_, value) ->
                    val barHeight = (value / 100f) * (canvasHeight - padding30)
                    val xOffset = index * (barWidth * 2) + barWidth / 2
                    val yOffset = canvasHeight - barHeight - padding20

                    // Draw Bar
                    drawRoundRect(
                        color = barColor,
                        topLeft = Offset(xOffset, yOffset),
                        size = Size(barWidth, barHeight),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                batchData.forEach { (label, value) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = SleekTextPrimary)
                        Text(text = "${value.toInt()}%", fontSize = 10.sp, color = SleekIndigo)
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyCodingActivityLineChart(
    weeklyData: List<Float> = listOf(12f, 25f, 40f, 32f, 55f, 80f, 95f),
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SleekSurface),
        border = BorderStroke(1.dp, SleekOutline)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "📈 Weekly Coding Activity & Executions",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = SleekTextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))

            val lineColor = SleekIndigo

            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                val canvasWidth = size.width
                val canvasHeight = size.height
                val maxVal = (weeklyData.maxOrNull() ?: 100f).coerceAtLeast(1f)

                val padding20 = 20.dp.toPx()
                val padding10 = 10.dp.toPx()

                val points = weeklyData.mapIndexed { index, value ->
                    val x = (index.toFloat() / (weeklyData.size - 1)) * canvasWidth
                    val y = canvasHeight - ((value / maxVal) * (canvasHeight - padding20)) - padding10
                    Offset(x, y)
                }

                val path = Path().apply {
                    points.forEachIndexed { i, pt ->
                        if (i == 0) moveTo(pt.x, pt.y) else lineTo(pt.x, pt.y)
                    }
                }

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 6f)
                )

                points.forEach { pt ->
                    drawCircle(color = lineColor, radius = 8f, center = pt)
                }
            }

            val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEach { day ->
                    Text(text = day, fontSize = 10.sp, color = SleekTextSecondary)
                }
            }
        }
    }
}

