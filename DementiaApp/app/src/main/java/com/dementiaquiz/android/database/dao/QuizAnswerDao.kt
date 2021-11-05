package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizAnswer
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
     fun getQuizAnswers():Flow<List<QuizAnswer>>

    @Query("SELECT * FROM quiz_answer WHERE resultId=:resultId")
     fun getQuizAnswersByResultId(resultId:Long):Flow<List<QuizAnswer>>

}