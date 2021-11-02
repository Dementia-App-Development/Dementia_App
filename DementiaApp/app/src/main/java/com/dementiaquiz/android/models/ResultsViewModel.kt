package com.dementiaquiz.android.models

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.*
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.database.model.User
import com.dementiaquiz.android.repositories.UserRepository

class ResultsViewModel(application : Application): AndroidViewModel(application) {

    private var userRepository: UserRepository? = null

    @SuppressLint("StaticFieldLeak")
    val context = getApplication<Application>().applicationContext

    fun getAllUsers(): LiveData<List<User>> {
        return userRepository!!.getUsers().asLiveData()
    }

    fun getAllNicknames(): LiveData<List<String>>{
        return userRepository!!.getAllNicknames().asLiveData()
    }

    fun setUserRepository(newUserRepository: UserRepository) {
        userRepository = newUserRepository
    }

}