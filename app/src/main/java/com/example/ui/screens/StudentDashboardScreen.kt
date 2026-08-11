package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.CModule
import com.example.data.model.UserProfile
import com.example.ui.components.WeeklyCodingActivityLineChart
import com.example.ui.theme.*

@Composable
fun StudentDashboardScreen(
    user: UserProfile,
    language: AppLanguage,
    modules: List<CModule>,
    onNavigateCompiler: () -> Unit,
    onNavigateTutor: () -> Unit,
    onNavigateLab: () -> Unit,
    onNavigateAssessments: () -> Unit,
    onNavigateAchievements: () -> Unit,
    onOpenExperiment: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Institution & Designer Header Banner
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SleekSurface),
                border = BorderStroke(1.dp, SleekOutline)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        border = BorderStroke(1.5.dp, SleekIndigo.copy(alpha = 0.3f)),
                        color = Color.White,
                        modifier = Modifier.size(46.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.iso_logo),
                            contentDescription = "ISO Degree College Logo",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(3.dp)
                                .clip(CircleShape)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "ISO Degree College",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SleekTextPrimary
                        )
                        Text(
                            text = "Department of Computer Science • C Programming Lab",
                            fontSize = 11.sp,
                            color = SleekTextSecondary
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SleekIndigoContainer,
                        border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.2f))
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "DESIGNER",
                                fontSize = 8.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SleekIndigo
                            )
                            Text(
                                text = "Makula Manohar",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextPrimary
                            )
                        }
                    }
                }
            }
        }

        // Gradient Hero Card - Current Progress
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(SleekIndigo, SleekViolet)
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column {
                                Text(
                                    text = if (language == AppLanguage.TELUGU) "ప్రస్తుత ప్రగతి" else "CURRENT PROGRESS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.2.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (language == AppLanguage.TELUGU) "మాడ్యూల్ 4: సి లో లూప్‌లు" else "Module 4: Loops in C",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            // Translucent Streak Pill
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(
                                        text = "🔥 ${user.streakDays} ${if (language == AppLanguage.TELUGU) "రోజుల స్ట్రీక్" else "Day Streak"}",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Custom Progress Track
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .fillMaxWidth(0.65f)
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Color.White)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (language == AppLanguage.TELUGU) "65% కోర్సు పూర్తయింది • 12 ల్యాబ్‌లు మిగిలి ఉన్నాయి" else "65% Course Completed • 12 Labs left",
                            fontSize = 12.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }
        }

        // Quick Learning Portals Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SleekToolCard(
                        title = if (language == AppLanguage.TELUGU) "యాక్టివ్ కంపైలర్" else "Active Compiler",
                        subtitle = "Last run: matrix_mul.c",
                        icon = Icons.Default.Terminal,
                        iconBg = SleekEmeraldContainer,
                        iconTint = SleekEmerald,
                        onClick = onNavigateCompiler,
                        modifier = Modifier.weight(1f)
                    )

                    SleekToolCard(
                        title = if (language == AppLanguage.TELUGU) "వర్చువల్ ల్యాబ్" else "Virtual Lab",
                        subtitle = "Exp 14: Nested Loops",
                        icon = Icons.Default.Science,
                        iconBg = SleekAmberContainer,
                        iconTint = SleekAmber,
                        onClick = onNavigateLab,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SleekToolCard(
                        title = if (language == AppLanguage.TELUGU) "AI ట్యూటర్ హెల్ప్" else "AI Tutor",
                        subtitle = "Bilingual Assistance",
                        icon = Icons.Default.Psychology,
                        iconBg = SleekIndigoContainer,
                        iconTint = SleekIndigo,
                        onClick = onNavigateTutor,
                        modifier = Modifier.weight(1f)
                    )

                    SleekToolCard(
                        title = if (language == AppLanguage.TELUGU) "పరీక్షలు & క్విజ్" else "Quizzes & Viva",
                        subtitle = "MCQs & Logic Tests",
                        icon = Icons.Default.Quiz,
                        iconBg = SleekRoseContainer,
                        iconTint = SleekRose,
                        onClick = onNavigateAssessments,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // AI Tutor Insights Live Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = SleekIndigoContainer),
                border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Pulsing Dot
                        val infiniteTransition = rememberInfiniteTransition()
                        val scale by infiniteTransition.animateFloat(
                            initialValue = 0.8f,
                            targetValue = 1.2f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            )
                        )
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .scale(scale)
                                .clip(CircleShape)
                                .background(SleekIndigo)
                        )
                        Text(
                            text = if (language == AppLanguage.TELUGU) "AI ట్యూటర్ సూచనలు" else "AI TUTOR INSIGHTS",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp,
                            color = SleekIndigo
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = SleekSurface),
                        border = BorderStroke(1.dp, SleekOutline)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "ERROR DETECTED IN LAST RUN",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextMuted
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "error: expected ';' before 'return'",
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Medium,
                                color = SleekRose
                            )
                            Spacer(modifier = Modifier.height(10.dp))

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SleekSurfaceVariant)
                                    .border(
                                        width = 1.dp,
                                        color = SleekOutline,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        text = "\"Add a semicolon after printf(). In C, every statement must end with ';'.\"",
                                        fontSize = 12.sp,
                                        color = SleekTextPrimary,
                                        lineHeight = 16.sp
                                    )
                                    Text(
                                        text = "\"printf() చివర ';' ఉపయోగించాలి. C లో ప్రతి స్టేట్మెంట్ ';' తో ముగియాలి.\"",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = SleekIndigo,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onNavigateTutor,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SleekSurface,
                            contentColor = SleekIndigo
                        ),
                        border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.3f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Psychology, contentDescription = null, modifier = Modifier.size(18.dp))
                            Text(
                                text = if (language == AppLanguage.TELUGU) "పూర్తి వివరాలు AI ని అడగండి" else "Ask AI for Line-by-Line Explanation",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Weekly Activity Chart
        item {
            WeeklyCodingActivityLineChart()
        }

        // Practical C Lab Modules
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (language == AppLanguage.TELUGU) "ప్రయోగాత్మక C ల్యాబ్ మాడ్యూళ్ళు" else "Practical C Lab Modules",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = SleekTextPrimary
                )
                TextButton(onClick = onNavigateLab) {
                    Text(
                        text = "View All 10",
                        color = SleekIndigo,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(modules) { module ->
                    Card(
                        modifier = Modifier
                            .width(230.dp)
                            .clickable { onOpenExperiment(module.experiments.firstOrNull()?.id ?: "") },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = SleekSurface),
                        border = BorderStroke(1.dp, SleekOutline)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(SleekIndigoContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "M${module.id}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekIndigo
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = if (language == AppLanguage.TELUGU) module.titleTe else module.titleEn,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextPrimary,
                                maxLines = 1
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = if (language == AppLanguage.TELUGU) module.descriptionTe else module.descriptionEn,
                                fontSize = 11.sp,
                                color = SleekTextSecondary,
                                maxLines = 2,
                                lineHeight = 15.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "${module.experiments.size} Experiments",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SleekIndigo
                                )
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = "Open",
                                    modifier = Modifier.size(16.dp),
                                    tint = SleekIndigo
                                )
                            }
                        }
                    }
                }
            }
        }

        // App Footer with Designer Attribution
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.2f)),
                    color = Color.White,
                    modifier = Modifier.size(32.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.iso_logo),
                        contentDescription = "ISO Degree College Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(CircleShape)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "ISO Degree College • Smart C Programming Laboratory",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SleekTextSecondary
                )
                Text(
                    text = "Designed by Makula Manohar",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SleekIndigo
                )
            }
        }
    }
}

@Composable
fun SleekToolCard(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    iconTint: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SleekSurface),
        border = BorderStroke(1.dp, SleekOutline)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(22.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = SleekTextPrimary,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = SleekTextSecondary
            )
        }
    }
}

