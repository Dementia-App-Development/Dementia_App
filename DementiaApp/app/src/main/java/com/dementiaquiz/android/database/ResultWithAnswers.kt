package com.dementiaquiz.android.database

import androidx.room.Embedded
import androidx.room.Relation

data class ResultWithAnswers(

    @Embedded
    val result: QuizResult,
    @Relation(
        parentColumn = "resultId",
        entityColumn = "resultId"
    )
    val answers: List<QuizAnswer>

)
