package com.dementiaquiz.android.fragments.adapters

import androidx.recyclerview.widget.DiffUtil
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.User

class QuizResultAdapter {

    class QuizResultComparator : DiffUtil.ItemCallback<QuizResult>() {

        override fun areItemsTheSame(oldQuizResult: QuizResult,newQuizResult: QuizResult): Boolean {

            return oldQuizResult === newQuizResult
        }

        override fun areContentsTheSame(oldQuizResult: QuizResult,newQuizResult: QuizResult): Boolean {
            return (oldQuizResult.resultId == newQuizResult.resultId)
                    && (oldQuizResult.score == newQuizResult.score)
                    && (oldQuizResult.timeCreated == newQuizResult.timeCreated)
                    && (oldQuizResult.userId == newQuizResult.userId)

        }
    }
}