package com.dementiaquiz.android.repositories

import androidx.annotation.WorkerThread
import androidx.room.Transaction
import com.dementiaquiz.android.database.dao.UserDao
import com.dementiaquiz.android.database.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class UserRepository(private val userDao: UserDao) {


    // check if there is already a same nickname in the database before insert, only
    // allow insert into the database if there is no such nickname.
    // return the ID of the inserted user if succeed, return -1 if failed
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    @Transaction
    suspend fun checkAndInsert(user:User): Long{

        val userWithSameNickname = userDao.getUserByNickname(user.nickname).first()

        if (userWithSameNickname!=null){
            // failed to insert, someone has used the user name
            return -1
        }else{
            // insert successfully, return the ID of the inserted user
            return userDao.insert(user)
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

    fun getUsers(): Flow<List<User>>{
        return userDao.getUsers()
    }


}