package com.dementiaquiz.android.database.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * This data class links a user to a result of a quiz
 */
data class UserWithResults (
    @Embedded
    val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "userId"
    )
    val results: List<QuizResult>

)
