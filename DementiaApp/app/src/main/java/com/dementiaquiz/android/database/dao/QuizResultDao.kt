package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.ResultWithAnswers

@Dao
interface QuizResultDao {

    @Insert
    fun insert(quizResult: QuizResult)

    @Delete
    fun delete(quizResult: QuizResult)

    @Update
    fun update(quizResult: QuizResult)

    @Query("SELECT * FROM quiz_result")
    fun getQuizResults():List<QuizResult>

    @Query("SELECT * FROM quiz_result WHERE userId = :userId")
    fun getQuizResultsByUserId(userId:Long):List<QuizResult>

    @Transaction
    @Query("SELECT * FROM quiz_result")
    fun getResultsWithAnswers(): List<ResultWithAnswers>

    @Transaction
    @Query("SELECT * FROM quiz_result WHERE resultId = :resultId")
    fun getResultWithAnswersByResultId(resultId: Long): ResultWithAnswers

    @Transaction
    @Query("SELECT * FROM quiz_result WHERE userId = :userId")
    fun getResultWithAnswersByUserId(userId: Long): List<ResultWithAnswers>


}