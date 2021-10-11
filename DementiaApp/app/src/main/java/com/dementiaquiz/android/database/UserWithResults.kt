package com.dementiaquiz.android.database

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithResults (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val results: List<QuizResult>

)
