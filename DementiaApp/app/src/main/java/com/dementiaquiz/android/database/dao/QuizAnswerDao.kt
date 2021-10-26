package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.ResultWithAnswers
import com.dementiaquiz.android.database.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizAnswerDao {

    @Insert
    suspend fun insert(quizAnswer: QuizAnswer)

    @Delete
    suspend fun delete(quizAnswer: QuizAnswer)

    @Update
    suspend fun update(quizAnswer: QuizAnswer)

    @Query("SELECT * FROM quiz_answer")
    suspend fun getQuizAnswers():Flow<List<QuizAnswer>>

    @Query("SELECT * FROM quiz_answer WHERE resultId=:resultId")
    suspend fun getQuizAnswersByResultId(resultId:Long):Flow<List<QuizAnswer>>

}