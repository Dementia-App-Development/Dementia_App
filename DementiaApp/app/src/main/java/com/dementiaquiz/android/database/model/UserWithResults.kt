package com.dementiaquiz.android.database.model

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
