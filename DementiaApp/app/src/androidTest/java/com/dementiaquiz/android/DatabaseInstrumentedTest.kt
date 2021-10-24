package com.dementiaquiz.android

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.dementiaquiz.android.database.QuizDatabase
import com.dementiaquiz.android.database.dao.QuizAnswerDao
import com.dementiaquiz.android.database.dao.QuizResultDao
import com.dementiaquiz.android.database.dao.UserDao
import com.dementiaquiz.android.database.model.User
import org.hamcrest.CoreMatchers.equalTo
import org.junit.After

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Before
import java.text.SimpleDateFormat
import java.util.*


@RunWith(AndroidJUnit4::class)
class DatabaseInstrumentedTest {

    private lateinit var quizAnswerDao: QuizAnswerDao
    private lateinit var quizResultDao: QuizResultDao
    private lateinit var userDao: UserDao

    private lateinit var db: QuizDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(
            context, QuizDatabase::class.java).build()

        userDao = db.userDao()
        quizAnswerDao = db.quizAnswerDao()
        quizResultDao = db.quizResultDao()

    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun writeUserAndReadInList() {

        val myDate = "2000/01/15"
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val dob: Date = sdf.parse(myDate)
//        val millis = dob.time

        Log.d("date of birth: ",dob.toString())
        val user1: User = User(10, "Rachel", "Wilcher",dob)
        val user2: User = User(0, "Stephen", "Cox",dob)

        userDao.insert(user1)
        userDao.insert(user2)
        val result = userDao.getUsers()

        assertThat(result.size, equalTo(2))
        
    }
}