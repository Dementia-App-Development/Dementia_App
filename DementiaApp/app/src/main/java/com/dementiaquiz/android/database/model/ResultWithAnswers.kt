package com.dementiaquiz.android.database.model

import androidx.room.Embedded
import androidx.room.Relation

/**
 * This data class is a quiz result with a set of answers
 */
data class ResultWithAnswers(

    @Embedded
    val result: QuizResult,
    @Relation(
        parentColumn = "resultId",
        entityColumn = "resultId"
    )
    val answers: List<QuizAnswer>

)
