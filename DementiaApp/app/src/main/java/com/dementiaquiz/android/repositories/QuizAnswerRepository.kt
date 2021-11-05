package com.dementiaquiz.android.repositories

import androidx.annotation.WorkerThread
import com.dementiaquiz.android.database.dao.QuizAnswerDao
import com.dementiaquiz.android.database.model.QuizAnswer
import kotlinx.coroutines.flow.Flow

class QuizAnswerRepository(private val quizAnswerDao: QuizAnswerDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(quizAnswer: QuizAnswer){
        quizAnswerDao.insert(quizAnswer)
    }

    fun getQuizAnswersByResultId(resultId:Long):Flow<List<QuizAnswer>> {
        return quizAnswerDao.getQuizAnswersByResultId(resultId)
    }
}