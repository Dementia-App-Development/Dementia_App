package com.dementiaquiz.android.repositories

import androidx.annotation.WorkerThread
import androidx.room.Transaction
import com.dementiaquiz.android.database.dao.UserDao
import com.dementiaquiz.android.database.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {


    // check if there is already a same nickname in the database before insert, only
    // allow insert into the database if there is no such nickname.
    // return true if insert succeed, false if failed
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    @Transaction
    suspend fun checkAndInsert(user:User): Boolean{

        val userWithSameNickname = userDao.getUserByNickname(user.nickname)
        if (userWithSameNickname!=null){
            return false
        }else{
            userDao.insert(user)
            return true
        }

    }

    fun getUserById(userId: Long): Flow<User>{
        return userDao.getUserById(userId)
    }

    fun getUserByNickname(nickname:String): Flow<User>{
        return userDao.getUserByNickname(nickname)
    }

    // get all nicknames in the database
    fun getAllNicknames(): Flow<List<String>>{
        return userDao.getAllNicknames()
    }


}