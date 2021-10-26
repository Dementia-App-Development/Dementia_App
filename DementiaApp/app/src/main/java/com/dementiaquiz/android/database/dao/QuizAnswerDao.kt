package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.ResultWithAnswers
import com.dementiaquiz.android.database.model.User

@Dao
interface QuizAnswerDao {

    @Insert
    fun insert(quizAnswer: QuizAnswer)

    @Delete
    fun delete(quizAnswer: QuizAnswer)

    @Update
    fun update(quizAnswer: QuizAnswer)

    @Query("SELECT * FROM quiz_answer")
    fun getQuizAnswers():List<QuizAnswer>

    @Query("SELECT * FROM quiz_answer WHERE resultId=:resultId")
    fun getQuizAnswersByResultId(resultId:Long):List<QuizAnswer>

}