package com.dementiaquiz.android.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizAnswer(

    @PrimaryKey(autoGenerate = true)
    val answerId: Long = 0L,
    val correct: Boolean,
    val response: String,
    val resultId: Long

)