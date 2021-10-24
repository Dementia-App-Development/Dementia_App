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
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
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

    @Test
    fun getQuizResultsByUserId(){

        val user1: User = User(10, "Rachel", "Wilcher",convertStringToDate("2021/10/24"))
        val user2: User = User(12, "Stephen", "Cox",convertStringToDate("2021/10/24"))

        userDao.insert(user1)
        userDao.insert(user2)

        val quizResult1:QuizResult = QuizResult(1,10,80,convertStringToDate("2021/10/24"))
        val quizResult2:QuizResult = QuizResult(2,12,50,convertStringToDate("2021/10/24"))

        quizResultDao.insert(quizResult1)
        quizResultDao.insert(quizResult2)

        val result = quizResultDao.getQuizResultsByUserId(10)
//        println(result)
        assertThat(result.get(0), equalTo(quizResult1))

    }

    @Test
    fun getQuizAnswersByResultId(){
        val quizResult1:QuizResult = QuizResult(1,10,80,convertStringToDate("2021/10/24"))
        val quizResult2:QuizResult = QuizResult(2,12,50,convertStringToDate("2021/10/24"))

        quizResultDao.insert(quizResult1)
        quizResultDao.insert(quizResult2)

        val quizAnswer1:QuizAnswer= QuizAnswer(0,"Which city are you in now?","Melbourne","Sydney",false,1)
        val quizAnswer2:QuizAnswer=QuizAnswer(0,"How old are you?","55","55",true,1)
        val quizAnswer3:QuizAnswer=QuizAnswer(0,"Which color is it?","Blue","Yellow",false,2)

        quizAnswerDao.insert(quizAnswer1)
        quizAnswerDao.insert(quizAnswer2)
        quizAnswerDao.insert(quizAnswer3)

        val result=quizAnswerDao.getQuizAnswersByResultId(1)
//        println(result)
        assertThat(result.size, equalTo(2))

    }



    // the parameter "dateStr" should be in the format of "yyyy/mm/dd"
    fun convertStringToDate(dateStr:String):Date{
        val sdf = SimpleDateFormat("yyyy/MM/dd")
        val date: Date = sdf.parse(dateStr)
        return date
    }
}