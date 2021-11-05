package com.dementiaquiz.android.repositories

import androidx.annotation.WorkerThread
import androidx.room.Transaction
import com.dementiaquiz.android.database.dao.QuizAnswerDao
import com.dementiaquiz.android.database.dao.QuizResultDao
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import kotlinx.coroutines.flow.Flow

// according to the app architecture guideline, Repository is an layer between the viewModel and
// the Room database. It encapsulates the data and then used as an API class for the viewModel
class QuizResultRepository(private val quizResultDao: QuizResultDao, private val answerDao: QuizAnswerDao) {

    // insertQuizResult and answers belong to that result. It returns the ID of
    // the inserted QuizResult
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    @Transaction
    suspend fun insertQuizResultAndAnswers(quizResult: QuizResult, quizAnswerList:List<QuizAnswer>): Long{

        val quizResultId = quizResultDao.insert(quizResult)
        for (quizAnswer in quizAnswerList) {
            quizAnswer.resultId = quizResultId
            answerDao.insert(quizAnswer)
        }
        return quizResultId

    }

    fun getQuizResults(): Flow<List<QuizResult>>{
        return quizResultDao.getQuizResults()
    }

    fun getQuizResultByResultId(resultId: Long):Flow<QuizResult>{
        return quizResultDao.getQuizResultByResultId(resultId)
    }

    fun getQuizResultsByUserId(userId:Long):Flow<List<QuizResult>>{
        return quizResultDao.getQuizResultsByUserId(userId)
    }

}