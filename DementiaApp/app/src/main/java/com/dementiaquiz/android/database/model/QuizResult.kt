package com.dementiaquiz.android.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * This data class represents a result to a single quiz with many questions
 */
@Entity(tableName = "quiz_result")
data class QuizResult(

    @PrimaryKey(autoGenerate = true)
    val resultId: Long = 0L,
    val userId: Long,
    val score:Int,
    val timeCreated:Date

)