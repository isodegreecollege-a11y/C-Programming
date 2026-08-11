package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FlowchartVisualizer(
    steps: List<String>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "💡 Algorithm Flowchart Visualization",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))

            steps.forEachIndexed { index, step ->
                val shapeColor = when {
                    index == 0 || index == steps.lastIndex -> Color(0xFF10B981) // Start/End Pill
                    step.contains("?") || step.contains("If") -> Color(0xFFF59E0B) // Decision Diamond/Card
                    else -> Color(0xFF0284C7) // Action Rectangle
                }

                Surface(
                    shape = if (index == 0 || index == steps.lastIndex) RoundedCornerShape(24.dp) else RoundedCornerShape(8.dp),
                    color = shapeColor,
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    Box(
                        modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = step,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (index < steps.lastIndex) {
                    Icon(
                        imageVector = Icons.Default.ArrowDownward,
                        contentDescription = "Flow Arrow",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 4.dp).size(20.dp)
                    )
                }
            }
        }
    }
}
