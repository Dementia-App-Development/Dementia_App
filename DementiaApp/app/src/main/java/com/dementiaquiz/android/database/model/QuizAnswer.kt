package com.dementiaquiz.android.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * This data class represents a single answer to a single quiz question object
 */
@Entity(tableName = "quiz_answer")
data class QuizAnswer(

    @PrimaryKey(autoGenerate = true)
    val answerId: Long = 0L,
    val questionDescription: String,
    val correctAnswer: String,
    val response: String,
    val correct: Boolean,
    var resultId: Long

)