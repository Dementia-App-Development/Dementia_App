package com.dementiaquiz.android.database.dao

import androidx.room.*
import com.dementiaquiz.android.database.model.User
import com.dementiaquiz.android.database.model.UserWithResults
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: User): Long

    @Delete
    suspend fun delete(user: User)

    @Update
    suspend fun update(user: User)

    // get all users in ascending order
    @Query("SELECT * FROM user ORDER BY nickname COLLATE NOCASE ASC")
     fun getUsers(): Flow<List<User>>

    @Query("SELECT * FROM User WHERE userId = :userId")
     fun getUserById(userId: Long): Flow<User>

    @Query("SELECT * FROM User WHERE nickname = :nickname")
    fun getUserByNickname(nickname:String): Flow<User>

    // get all nicknames in ascending order
    @Query("SELECT nickname FROM User ORDER BY nickname COLLATE NOCASE ASC")
    fun getAllNicknames(): Flow<List<String>>

    @Transaction
    @Query("SELECT * FROM user")
     fun getUsersWithResults():Flow<List<UserWithResults>>

    @Transaction
    @Query("SELECT * FROM user WHERE userId==:userId")
     fun getUsersWithResultsByUserId(userId:Long):Flow<UserWithResults>

}