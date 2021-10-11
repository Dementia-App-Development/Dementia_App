package com.dementiaquiz.android.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, QuizResult::class, QuizAnswer::class], version = 1, exportSchema = false)
abstract class QuizDatabase : RoomDatabase() {

    abstract val quizDatabase: QuizDatabase

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
}