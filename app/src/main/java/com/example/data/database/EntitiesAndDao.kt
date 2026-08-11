package com.example.data.database

import androidx.room.*
import com.example.data.model.LabSubmission
import com.example.data.model.ProgramRecord
import com.example.data.model.UserBadge
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgramDao {
    @Query("SELECT * FROM program_records WHERE userId = :userId ORDER BY timestamp DESC")
    fun getProgramsByUser(userId: String): Flow<List<ProgramRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgram(program: ProgramRecord): Long

    @Query("DELETE FROM program_records WHERE id = :id")
    suspend fun deleteProgram(id: Long)
}

@Dao
interface SubmissionDao {
    @Query("SELECT * FROM lab_submissions ORDER BY submittedAt DESC")
    fun getAllSubmissions(): Flow<List<LabSubmission>>

    @Query("SELECT * FROM lab_submissions WHERE studentId = :studentId ORDER BY submittedAt DESC")
    fun getSubmissionsByStudent(studentId: String): Flow<List<LabSubmission>>

    @Query("SELECT * FROM lab_submissions WHERE experimentId = :experimentId AND studentId = :studentId LIMIT 1")
    suspend fun getSubmissionForExperiment(experimentId: String, studentId: String): LabSubmission?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubmission(submission: LabSubmission)

    @Query("UPDATE lab_submissions SET teacherMarks = :marks, teacherComment = :comment WHERE id = :submissionId")
    suspend fun updateTeacherEvaluation(submissionId: String, marks: Int, comment: String)
}

@Dao
interface BadgeDao {
    @Query("SELECT * FROM unlocked_badges")
    fun getAllBadges(): Flow<List<UserBadge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateBadges(badges: List<UserBadge>)

    @Query("UPDATE unlocked_badges SET isUnlocked = 1, unlockedAt = :timestamp WHERE id = :badgeId")
    suspend fun unlockBadge(badgeId: String, timestamp: Long = System.currentTimeMillis())
}

@Database(
    entities = [ProgramRecord::class, LabSubmission::class, UserBadge::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun programDao(): ProgramDao
    abstract fun submissionDao(): SubmissionDao
    abstract fun badgeDao(): BadgeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: android.content.Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ai_c_lab_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
