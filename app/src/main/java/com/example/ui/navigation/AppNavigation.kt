package com.example.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.data.database.AppDatabase

import com.example.data.model.*
import com.example.data.repository.CLabRepository
import com.example.ui.components.TopAppBarWithLanguage
import com.example.ui.screens.*

import com.example.ui.theme.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object StudentDashboard : Screen("student_dashboard", "Home", Icons.Default.GridView)
    object Compiler : Screen("compiler", "Compiler", Icons.Default.Code)
    object AiTutor : Screen("ai_tutor", "AI Tutor", Icons.Default.Psychology)
    object VirtualLab : Screen("virtual_lab", "Lessons", Icons.Default.MenuBook)
    object Achievements : Screen("achievements", "Profile", Icons.Default.Person)
    object Assessments : Screen("assessments", "Quizzes", Icons.Default.Quiz)
    object ExperimentDetail : Screen("exp_detail", "Experiment", Icons.Default.Science)
    object TeacherDashboard : Screen("teacher_dashboard", "Teacher", Icons.Default.Assessment)
    object TeacherSubmissions : Screen("teacher_submissions", "Submissions", Icons.Default.RateReview)
    object TeacherAnalytics : Screen("teacher_analytics", "Analytics", Icons.Default.Analytics)
    object AdminPanel : Screen("admin_panel", "Admin", Icons.Default.AdminPanelSettings)
}

@Composable
fun AppNavigation(
    repository: CLabRepository
) {
    val currentUser by repository.currentUser.collectAsState()
    val currentLanguage by repository.currentLanguage.collectAsState()

    val savedPrograms by repository.getSavedPrograms("std_101").collectAsState(initial = emptyList())
    val submissions by repository.getAllSubmissions().collectAsState(initial = emptyList())
    val badges by repository.getUserBadges().collectAsState(
        initial = repository.initialBadges
    )

    var currentRoute by remember { mutableStateOf(Screen.StudentDashboard.route) }
    var selectedExperiment by remember { mutableStateOf<Experiment?>(null) }
    var tutorCodeContext by remember { mutableStateOf("") }
    var tutorErrorContext by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBarWithLanguage(
                user = currentUser,
                currentLanguage = currentLanguage,
                onLanguageToggle = { repository.setLanguage(it) },
                onRoleSelected = { role ->
                    repository.switchRole(role)
                    currentRoute = when (role) {
                        UserRole.STUDENT -> Screen.StudentDashboard.route
                        UserRole.TEACHER -> Screen.TeacherDashboard.route
                        UserRole.COLLEGE_ADMIN, UserRole.SUPER_ADMIN -> Screen.AdminPanel.route
                    }
                },
                onOpenProfile = {}
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = SleekSurface,
                contentColor = SleekTextPrimary,
                tonalElevation = 0.dp,
                windowInsets = WindowInsets.navigationBars
            ) {
                val navItemColors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SleekIndigo,
                    selectedTextColor = SleekIndigo,
                    indicatorColor = SleekIndigoContainer,
                    unselectedIconColor = SleekTextMuted,
                    unselectedTextColor = SleekTextMuted
                )

                if (currentUser.role == UserRole.STUDENT) {
                    val navItems = listOf(
                        Screen.StudentDashboard,
                        Screen.VirtualLab,
                        Screen.Compiler,
                        Screen.AiTutor,
                        Screen.Achievements
                    )
                    navItems.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = { currentRoute = screen.route },
                            icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = navItemColors
                        )
                    }
                } else if (currentUser.role == UserRole.TEACHER) {
                    val teacherNavItems = listOf(
                        Screen.TeacherDashboard,
                        Screen.TeacherSubmissions,
                        Screen.TeacherAnalytics
                    )
                    teacherNavItems.forEach { screen ->
                        NavigationBarItem(
                            selected = currentRoute == screen.route,
                            onClick = { currentRoute = screen.route },
                            icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                            label = { Text(screen.title, fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                            colors = navItemColors
                        )
                    }
                } else {
                    NavigationBarItem(
                        selected = true,
                        onClick = {},
                        icon = { Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = "Admin") },
                        label = { Text("Admin Panel", fontSize = 10.sp, fontWeight = FontWeight.Bold) },
                        colors = navItemColors
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentRoute) {
                Screen.StudentDashboard.route -> {
                    StudentDashboardScreen(
                        user = currentUser,
                        language = currentLanguage,
                        modules = repository.modules,
                        onNavigateCompiler = { currentRoute = Screen.Compiler.route },
                        onNavigateTutor = { currentRoute = Screen.AiTutor.route },
                        onNavigateLab = { currentRoute = Screen.VirtualLab.route },
                        onNavigateAssessments = { currentRoute = Screen.Assessments.route },
                        onNavigateAchievements = { currentRoute = Screen.Achievements.route },
                        onOpenExperiment = { expId ->
                            val exp = repository.modules.flatMap { it.experiments }.find { it.id == expId }
                            if (exp != null) {
                                selectedExperiment = exp
                                currentRoute = Screen.ExperimentDetail.route
                            }
                        }
                    )
                }

                Screen.Compiler.route -> {
                    CCompilerScreen(
                        language = currentLanguage,
                        onSaveProgram = { record ->
                            coroutineScope.launch {
                                repository.saveProgram(record)
                            }
                        },
                        onNavigateAiTutorWithCode = { code, err ->
                            tutorCodeContext = code
                            tutorErrorContext = err
                            currentRoute = Screen.AiTutor.route
                        }
                    )
                }

                Screen.AiTutor.route -> {
                    AiTutorScreen(
                        initialCode = tutorCodeContext,
                        initialError = tutorErrorContext,
                        language = currentLanguage
                    )
                }

                Screen.VirtualLab.route -> {
                    VirtualLabScreen(
                        modules = repository.modules,
                        language = currentLanguage,
                        onOpenExperiment = { exp ->
                            selectedExperiment = exp
                            currentRoute = Screen.ExperimentDetail.route
                        }
                    )
                }

                Screen.ExperimentDetail.route -> {
                    val exp = selectedExperiment ?: repository.modules.first().experiments.first()
                    ExperimentDetailScreen(
                        experiment = exp,
                        language = currentLanguage,
                        onSubmitRecord = { sub ->
                            coroutineScope.launch {
                                repository.submitLabRecord(sub)
                            }
                        },
                        onNavigateBack = { currentRoute = Screen.VirtualLab.route }
                    )
                }


                Screen.Assessments.route -> {
                    AssessmentsScreen(
                        quizzes = repository.quizzes,
                        language = currentLanguage
                    )
                }

                Screen.Achievements.route -> {
                    AchievementsScreen(
                        user = currentUser,
                        badges = badges,
                        language = currentLanguage
                    )
                }

                Screen.TeacherDashboard.route -> {
                    TeacherDashboardScreen(
                        user = currentUser,
                        language = currentLanguage,
                        onNavigateSubmissions = { currentRoute = Screen.TeacherSubmissions.route },
                        onNavigateAnalytics = { currentRoute = Screen.TeacherAnalytics.route }
                    )
                }

                Screen.TeacherSubmissions.route -> {
                    TeacherSubmissionsScreen(
                        submissions = submissions.ifEmpty {
                            listOf(
                                LabSubmission(
                                    id = "sub_demo_1",
                                    experimentId = "exp_101",
                                    studentId = "std_101",
                                    studentName = "Sai Kumar Reddy",
                                    studentRollNo = "2024CS101",
                                    code = "#include <stdio.h>\nint main() {\n    printf(\"Hello World! Welcome to AI C Lab.\\n\");\n    return 0;\n}",
                                    compilationStatus = "SUCCESS",
                                    testCasesPassed = 1,
                                    totalTestCases = 1,
                                    score = 95,
                                    aiFeedbackEn = "Perfect code execution.",
                                    aiFeedbackTe = "కోడ్ సక్రమంగా రన్ అయ్యింది."
                                )
                            )
                        },
                        language = currentLanguage,
                        onEvaluateSubmission = { subId, marks, comment ->
                            coroutineScope.launch {
                                repository.updateTeacherGrade(subId, marks, comment)
                            }
                        },

                        onNavigateBack = { currentRoute = Screen.TeacherDashboard.route }
                    )
                }

                Screen.TeacherAnalytics.route -> {
                    TeacherAnalyticsScreen(
                        onNavigateBack = { currentRoute = Screen.TeacherDashboard.route }
                    )
                }

                Screen.AdminPanel.route -> {
                    AdminPanelScreen(user = currentUser)
                }
            }
        }
    }
}
