package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.User
import com.dementiaquiz.android.database.model.UserWithResults

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM user")
    suspend fun getUsers():List<User>

    @Query("SELECT * FROM User WHERE userId = :userId")
    suspend fun getUserById(userId: Long): User

    @Transaction
    @Query("SELECT * FROM user")
    suspend fun getUsersWithResults():List<UserWithResults>

    @Transaction
    @Query("SELECT * FROM user WHERE userId==:userId")
    suspend fun getUsersWithResultsByUserId(userId:Long):UserWithResults

}