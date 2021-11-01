package com.dementiaquiz.android.models

import androidx.lifecycle.*
import com.dementiaquiz.android.database.model.User
import com.dementiaquiz.android.repositories.QuizResultRepository
import com.dementiaquiz.android.repositories.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UsersViewModel(private val userRepository: UserRepository): ViewModel() {

    // check if there is already a same nickname in the database before insert, only
    // allow insert into the database if there is no such nickname.
    // return true if insert succeed, false if failed
    fun checkAndInsert(user: User):Boolean {

        var result = false;
        viewModelScope.launch {
            result=userRepository.checkAndInsert(user)
        }
        return result
    }

    fun getUserByNickname(nickname:String): LiveData<User> {
        return userRepository.getUserByNickname(nickname).asLiveData()
    }

    // get all nicknames in the database
    fun getAllNicknames(): LiveData<List<String>>{
        return userRepository.getAllNicknames().asLiveData()
    }

}

class UsersViewModelModelFactory(private val userRepository: UserRepository):
    ViewModelProvider.Factory{

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(UsersViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return UsersViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}