package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AppLanguage
import com.example.data.model.CertificateData
import com.example.data.model.UserBadge
import com.example.data.model.UserProfile
import com.example.ui.components.CertificateView
import com.example.ui.theme.*

@Composable
fun AchievementsScreen(
    user: UserProfile,
    badges: List<UserBadge>,
    language: AppLanguage
) {
    var showCertificateView by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SleekBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
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
                        Column {
                            Text(
                                text = "🏆 Badges & Leaderboard",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SleekIndigo
                            )
                            Text(
                                text = "Rank #1 in ISO Degree College • ${user.totalPoints} Points",
                                fontSize = 12.sp,
                                color = SleekTextSecondary
                            )
                        }

                        Button(
                            onClick = { showCertificateView = !showCertificateView },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SleekAmber)
                        ) {
                            Text("Certificate", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        if (showCertificateView) {
            item {
                CertificateView(
                    cert = CertificateData(
                        studentName = user.name,
                        rollNumber = user.rollNo,
                        courseName = user.course,
                        collegeName = user.collegeName,
                        completionDate = "10 August 2026",
                        certificateId = "AICLAB-2026-9081",
                        scorePercentage = 94,
                        labHoursCompleted = 45
                    ),
                    onDownload = {}
                )
            }
        }

        // Badges Section
        item {
            Text("🏅 Unlocked Badges", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SleekTextPrimary)
            Spacer(modifier = Modifier.height(10.dp))

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                badges.forEach { badge ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (badge.isUnlocked) SleekEmeraldContainer else SleekSurface
                        ),
                        border = BorderStroke(1.dp, if (badge.isUnlocked) SleekEmerald.copy(alpha = 0.2f) else SleekOutline)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(if (badge.isUnlocked) SleekEmerald else Color.Gray),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (badge.isUnlocked) Icons.Default.EmojiEvents else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }

                            Column {
                                Text(
                                    text = if (language == AppLanguage.TELUGU) badge.nameTe else badge.nameEn,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SleekTextPrimary
                                )
                                Text(
                                    text = if (language == AppLanguage.TELUGU) badge.descTe else badge.descEn,
                                    fontSize = 11.sp,
                                    color = SleekTextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Leaderboard Section
        item {
            Text("📊 Student Leaderboard", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SleekTextPrimary)
            Spacer(modifier = Modifier.height(10.dp))

            val leaderboard = listOf(
                Triple("1", "Sai Kumar Reddy (You)", "850 pts"),
                Triple("2", "K. Anusha", "810 pts"),
                Triple("3", "M. Rahul Varma", "780 pts"),
                Triple("4", "P. Suresh", "740 pts")
            )

            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = SleekSurface),
                border = BorderStroke(1.dp, SleekOutline)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    leaderboard.forEach { (rank, name, pts) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(text = "#$rank", fontWeight = FontWeight.ExtraBold, color = SleekIndigo)
                                Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = SleekTextPrimary)
                            }
                            Text(text = pts, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = SleekEmerald)
                        }
                        HorizontalDivider(color = SleekOutline)
                    }
                }
            }
        }
    }
}

