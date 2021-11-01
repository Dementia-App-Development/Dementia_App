package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.User
import com.dementiaquiz.android.database.model.UserWithResults
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User)

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM user")
     fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE userId = :userId")
     fun getUserById(userId: Long): Flow<User>

    @Query("SELECT * FROM User WHERE nickname = :nickname")
    fun getUserByNickname(nickname:String): Flow<User>

    //TODO: test this function
    @Query("SELECT nickname FROM User")
    fun getNicknames(): Flow<List<String>>

    @Transaction
    @Query("SELECT * FROM user")
     fun getUsersWithResults():Flow<List<UserWithResults>>

    @Transaction
    @Query("SELECT * FROM user WHERE userId==:userId")
     fun getUsersWithResultsByUserId(userId:Long):Flow<UserWithResults>

}