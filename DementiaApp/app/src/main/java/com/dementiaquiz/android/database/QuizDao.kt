package com.dementiaquiz.android.database

import androidx.room.*
import com.dementiaquiz.android.database.QuizResult

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