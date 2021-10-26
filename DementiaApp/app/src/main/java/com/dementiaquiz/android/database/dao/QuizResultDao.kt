package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.ResultWithAnswers

@Dao
interface QuizResultDao {

    @Insert
    suspend fun insert(quizResult: QuizResult)

    @Delete
    suspend fun delete(quizResult: QuizResult)

    @Update
    suspend fun update(quizResult: QuizResult)

    @Query("SELECT * FROM quiz_result")
    suspend fun getQuizResults():List<QuizResult>

    @Query("SELECT * FROM quiz_result WHERE userId = :userId")
    suspend fun getQuizResultsByUserId(userId:Long):List<QuizResult>

    @Transaction
    @Query("SELECT * FROM quiz_result")
    suspend fun getResultsWithAnswers(): List<ResultWithAnswers>

    @Transaction
    @Query("SELECT * FROM quiz_result WHERE resultId = :resultId")
    suspend fun getResultWithAnswersByResultId(resultId: Long): ResultWithAnswers

    @Transaction
    @Query("SELECT * FROM quiz_result WHERE userId = :userId")
    suspend fun getResultWithAnswersByUserId(userId: Long): List<ResultWithAnswers>


}