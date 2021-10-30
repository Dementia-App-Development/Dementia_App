package com.dementiaquiz.android

import android.app.Application
import com.dementiaquiz.android.database.QuizDatabase
import com.dementiaquiz.android.repositories.QuizResultRepository
import timber.log.Timber

class DementiaQuizApplication : Application() {

    // cited from "https://developer.android.com/codelabs/android-room-with-a-view-kotlin#12"
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    val database by lazy{QuizDatabase.getInstance(this)}
    val quizResultRepository by lazy { QuizResultRepository(database.quizResultDao()) }

    override fun onCreate() {
        super.onCreate()

        // Initialize timber
        Timber.plant(Timber.DebugTree())
        Timber.i("OnCreate called")
    }
}