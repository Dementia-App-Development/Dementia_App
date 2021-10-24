package com.dementiaquiz.android.database.model

import androidx.room.Embedded
import androidx.room.Relation
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult

data class ResultWithAnswers(

    @Embedded
    val result: QuizResult,
    @Relation(
        parentColumn = "resultId",
        entityColumn = "resultId"
    )
    val answers: List<QuizAnswer>

)
