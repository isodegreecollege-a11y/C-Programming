package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    STUDENT,
    TEACHER,
    COLLEGE_ADMIN,
    SUPER_ADMIN
}

enum class AppLanguage {
    ENGLISH,
    TELUGU
}

data class UserProfile(

    val id: String,
    val name: String,
    val email: String,
    val rollNo: String,
    val role: UserRole,
    val course: String = "B.Sc Computer Science",
    val section: String = "Section A",
    val batch: String = "2024-2027",
    val collegeName: String = "ISO Degree & PG College",
    val streakDays: Int = 5,
    val totalPoints: Int = 450,
    val completedLabsCount: Int = 18,
    val totalProgramsExecuted: Int = 64
)

data class TestCase(
    val id: Int = 0,
    val input: String,
    val expectedOutput: String,
    val isHidden: Boolean = false
)

data class VivaQuestion(
    val id: Int,
    val questionEn: String,
    val questionTe: String,
    val answerEn: String,
    val answerTe: String
)

data class Experiment(
    val id: String,
    val moduleId: String,
    val expNo: Int,
    val titleEn: String,
    val titleTe: String,
    val objectiveEn: String,
    val objectiveTe: String,
    val theoryEn: String,
    val theoryTe: String,
    val algorithmEn: List<String>,
    val algorithmTe: List<String>,
    val flowchartSteps: List<String>,
    val sampleCode: String,
    val sampleInput: String,
    val sampleOutput: String,
    val testCases: List<TestCase>,
    val vivaQuestions: List<VivaQuestion>,
    val commonErrorsEn: String = "",
    val commonErrorsTe: String = ""
)

data class CModule(
    val id: String,
    val moduleNo: Int,
    val titleEn: String,
    val titleTe: String,
    val descriptionEn: String,
    val descriptionTe: String,
    val iconName: String,
    val experiments: List<Experiment>
)

@Entity(tableName = "program_records")
data class ProgramRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String,
    val title: String,
    val code: String,
    val input: String = "",
    val output: String = "",
    val executionTimeMs: Long = 12,
    val memoryUsageKb: Long = 1024,
    val hasError: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "lab_submissions")
data class LabSubmission(
    @PrimaryKey val id: String,
    val experimentId: String,
    val studentId: String,
    val studentName: String,
    val studentRollNo: String,
    val code: String,
    val compilationStatus: String, // "SUCCESS" or "FAILED"
    val testCasesPassed: Int,
    val totalTestCases: Int,
    val score: Int, // 0 - 100
    val aiFeedbackEn: String,
    val aiFeedbackTe: String,
    val teacherMarks: Int? = null,
    val teacherComment: String? = null,
    val submittedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "unlocked_badges")
data class UserBadge(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameTe: String,
    val descEn: String,
    val descTe: String,
    val iconName: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

data class QuizQuestion(
    val id: Int,
    val questionEn: String,
    val questionTe: String,
    val optionsEn: List<String>,
    val optionsTe: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationTe: String
)

data class QuizModule(
    val id: String,
    val titleEn: String,
    val titleTe: String,
    val questions: List<QuizQuestion>,
    val timeLimitMinutes: Int = 10
)

data class CertificateData(
    val studentName: String,
    val rollNumber: String,
    val courseName: String,
    val collegeName: String,
    val completionDate: String,
    val certificateId: String,
    val scorePercentage: Int,
    val labHoursCompleted: Int
)
