package com.dementiaquiz.android.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class QuizResult(

    @PrimaryKey(autoGenerate = true)
    val resultId: Long = 0L,
    val userId: Long,
    val score:Int,
    val timeCreated:Date

)