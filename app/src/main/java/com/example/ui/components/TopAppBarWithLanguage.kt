package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.AppLanguage
import com.example.data.model.UserProfile
import com.example.data.model.UserRole
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWithLanguage(
    user: UserProfile,
    currentLanguage: AppLanguage,
    onLanguageToggle: (AppLanguage) -> Unit,
    onRoleSelected: (UserRole) -> Unit,
    onOpenProfile: () -> Unit
) {
    var showRoleMenu by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }

    Surface(
        color = SleekSurface,
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, SleekOutline)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Logo & Title
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.clickable { showAboutDialog = true }
            ) {
                Surface(
                    shape = CircleShape,
                    border = BorderStroke(1.5.dp, SleekIndigo.copy(alpha = 0.3f)),
                    color = Color.White,
                    modifier = Modifier.size(38.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.iso_logo),
                        contentDescription = "ISO Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(2.dp)
                            .clip(CircleShape)
                    )
                }

                Column {
                    Text(
                        text = "ISO DEGREE COLLEGE",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.2.sp,
                        color = SleekIndigo
                    )
                    Text(
                        text = if (currentLanguage == AppLanguage.TELUGU) "స్మార్ట్ C ల్యాబ్" else "AI C Lab",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SleekTextPrimary
                    )
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Bilingual Language Switcher Pill
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFF1F5F9),
                    border = BorderStroke(1.dp, SleekOutline),
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            val nextLang = if (currentLanguage == AppLanguage.ENGLISH) AppLanguage.TELUGU else AppLanguage.ENGLISH
                            onLanguageToggle(nextLang)
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "EN",
                            fontSize = 11.sp,
                            fontWeight = if (currentLanguage == AppLanguage.ENGLISH) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentLanguage == AppLanguage.ENGLISH) SleekIndigo else SleekTextSecondary
                        )
                        Text(
                            text = "|",
                            fontSize = 11.sp,
                            color = SleekTextMuted
                        )
                        Text(
                            text = "తెలుగు",
                            fontSize = 11.sp,
                            fontWeight = if (currentLanguage == AppLanguage.TELUGU) FontWeight.Bold else FontWeight.Normal,
                            color = if (currentLanguage == AppLanguage.TELUGU) SleekIndigo else SleekTextSecondary
                        )
                    }
                }

                // Role Dropdown Pill
                Box {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = SleekIndigoContainer,
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { showRoleMenu = true }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = when (user.role) {
                                    UserRole.STUDENT -> "Student"
                                    UserRole.TEACHER -> "Teacher"
                                    UserRole.COLLEGE_ADMIN -> "College Admin"
                                    UserRole.SUPER_ADMIN -> "Super Admin"
                                },
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekIndigo
                            )
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Switch Portal Role",
                                modifier = Modifier.size(16.dp),
                                tint = SleekIndigo
                            )
                        }
                    }

                    DropdownMenu(
                        expanded = showRoleMenu,
                        onDismissRequest = { showRoleMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("🎓 Student Portal") },
                            onClick = {
                                onRoleSelected(UserRole.STUDENT)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("👨‍🏫 Teacher Portal") },
                            onClick = {
                                onRoleSelected(UserRole.TEACHER)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("🏛️ College Admin") },
                            onClick = {
                                onRoleSelected(UserRole.COLLEGE_ADMIN)
                                showRoleMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("⚙️ Super Admin") },
                            onClick = {
                                onRoleSelected(UserRole.SUPER_ADMIN)
                                showRoleMenu = false
                            }
                        )
                    }
                }

                // User Avatar Box
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(SleekIndigoContainer)
                        .border(1.5.dp, SleekIndigo.copy(alpha = 0.3f), CircleShape)
                        .clickable { showAboutDialog = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.split(" ").mapNotNull { it.firstOrNull() }.take(2).joinToString("").uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = SleekIndigo,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }

    // App Information & Designer Credits Dialog
    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = { showAboutDialog = false },
            confirmButton = {
                Button(
                    onClick = { showAboutDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = SleekIndigo),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Close", fontWeight = FontWeight.Bold)
                }
            },
            icon = {
                Surface(
                    shape = CircleShape,
                    border = BorderStroke(2.dp, SleekIndigo),
                    color = Color.White,
                    modifier = Modifier.size(64.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.iso_logo),
                        contentDescription = "ISO Degree College Logo",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(4.dp)
                            .clip(CircleShape)
                    )
                }
            },
            title = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "ISO Degree College",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SleekTextPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Smart C Programming Laboratory",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = SleekIndigo,
                        textAlign = TextAlign.Center
                    )
                }
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SleekIndigoContainer,
                        border = BorderStroke(1.dp, SleekIndigo.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "🎨 DESIGNER CREDIT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.2.sp,
                                color = SleekIndigo
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Makula Manohar",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextPrimary
                            )
                            Text(
                                text = "Lead UI/UX Designer & Architect",
                                fontSize = 11.sp,
                                color = SleekTextSecondary
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SleekSurfaceVariant,
                        border = BorderStroke(1.dp, SleekOutline),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "👤 Logged In As: ${user.name}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SleekTextPrimary
                            )
                            Text(
                                text = "Roll No: ${user.rollNo} • ISO CS Department",
                                fontSize = 11.sp,
                                color = SleekTextSecondary
                            )
                        }
                    }
                }
            },
            containerColor = SleekSurface,
            shape = RoundedCornerShape(24.dp)
        )
    }
}


