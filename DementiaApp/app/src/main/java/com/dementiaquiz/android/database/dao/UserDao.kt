package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.User
import com.dementiaquiz.android.database.model.UserWithResults

@Dao
interface UserDao {

    @Insert
    fun insert(user: User)

    @Delete
    fun delete(user: User)

    @Update
    fun update(user: User)

    @Query("SELECT * FROM user")
    fun getUsers():List<User>

    @Query("SELECT * FROM User WHERE userId = :userId")
    fun getUserById(userId: Long): User

    @Transaction
    @Query("SELECT * FROM user")
    fun getUsersWithResults():List<UserWithResults>

    @Transaction
    @Query("SELECT * FROM user WHERE userId==:userId")
    fun getUsersWithResultsByUserId(userId:Long):UserWithResults

}