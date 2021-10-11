package com.dementiaquiz.android.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class QuizResult(

    @PrimaryKey(autoGenerate = true)
    val resultId: Long = 0L,
    val userId: Long

)