package com.dementiaquiz.android.repositories

import androidx.annotation.WorkerThread
import com.dementiaquiz.android.database.dao.QuizResultDao
import com.dementiaquiz.android.database.model.QuizResult
import kotlinx.coroutines.flow.Flow

// according to the app architecture guideline, Repository is an layer between the viewModel and
// the Room database. It encapsulates the data and then used as an API class for the viewModel
class QuizResultRepository(private val quizResultDao: QuizResultDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(quizResult: QuizResult){
        quizResultDao.insert(quizResult)
    }

    fun getQuizResults(): Flow<List<QuizResult>>{
        return quizResultDao.getQuizResults()
    }

    fun getQuizResultsByUserId(userId:Long):Flow<List<QuizResult>>{
        return quizResultDao.getQuizResultsByUserId(userId)
    }


}