package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.ResultWithAnswers
import com.dementiaquiz.android.database.model.User

@Dao
interface QuizDao {

    @Insert
    fun insert(user: User)

    @Insert
    fun insert(result: QuizResult)

    @Insert
    fun insert(answer: QuizAnswer)

    @Update
    fun update(user: User)

    @Update
    fun update(result: QuizResult)

    @Update
    fun update(answer: QuizAnswer)

    @Query("SELECT * FROM User WHERE userId = :key")
    fun getUser(key: Long): User

    @Transaction
    @Query("SELECT * FROM QuizResult WHERE resultId = :key")
    fun getResultWithAnswers(key: Long): ResultWithAnswers
}