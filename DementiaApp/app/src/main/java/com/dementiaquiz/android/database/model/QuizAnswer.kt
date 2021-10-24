package com.dementiaquiz.android.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizAnswer(

    @PrimaryKey(autoGenerate = true)
    val answerId: Long = 0L,
    val questionDescription: String,
    val correctAnswer: String,
    val response: String,
    val correct: Boolean,
    val resultId: Long

)