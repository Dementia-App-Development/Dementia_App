package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.ResultWithAnswers
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {

    @Insert
    suspend fun insert(quizResult: QuizResult)

    @Delete
    suspend fun delete(quizResult: QuizResult)

    @Update
    suspend fun update(quizResult: QuizResult)

    @Query("SELECT * FROM quiz_result")
     fun getQuizResults():Flow<List<QuizResult>>

    @Query("SELECT * FROM quiz_result WHERE resultId = :resultId")
    fun getQuizResultByResultId(resultId: Long):Flow<QuizResult>

    @Query("SELECT * FROM quiz_result WHERE userId = :userId")
     fun getQuizResultsByUserId(userId:Long):Flow<List<QuizResult>>

    @Transaction
    @Query("SELECT * FROM quiz_result")
     fun getResultsWithAnswers(): Flow<List<ResultWithAnswers>>

    @Transaction
    @Query("SELECT * FROM quiz_result WHERE resultId = :resultId")
     fun getResultWithAnswersByResultId(resultId: Long): Flow<ResultWithAnswers>

    @Transaction
    @Query("SELECT * FROM quiz_result WHERE userId = :userId")
     fun getResultWithAnswersByUserId(userId: Long): Flow<List<ResultWithAnswers>>


}