package com.dementiaquiz.android

import android.app.Application
import timber.log.Timber

class DementiaQuizApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize timber
        Timber.plant(Timber.DebugTree())
        Timber.i("OnCreate called")
    }
}