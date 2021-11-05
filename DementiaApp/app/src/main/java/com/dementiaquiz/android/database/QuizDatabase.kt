package com.dementiaquiz.android.database

import android.content.Context
import androidx.room.*
import com.dementiaquiz.android.database.dao.QuizAnswerDao
import com.dementiaquiz.android.database.dao.QuizResultDao
import com.dementiaquiz.android.database.dao.UserDao
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.database.model.User
import com.dementiaquiz.android.database.typeConverter.Converter

/**
 * An abstract class representing a quiz database
 * Use getInstance to create a database for this class
 */
@Database(entities = [User::class, QuizResult::class, QuizAnswer::class], version = 5, exportSchema = false)
@TypeConverters(Converter::class)
abstract class QuizDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: QuizDatabase? = null

        fun getInstance(context: Context): QuizDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        QuizDatabase::class.java,
                        "quiz_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }

    abstract fun quizAnswerDao():QuizAnswerDao
    abstract fun quizResultDao():QuizResultDao
    abstract fun userDao():UserDao

}