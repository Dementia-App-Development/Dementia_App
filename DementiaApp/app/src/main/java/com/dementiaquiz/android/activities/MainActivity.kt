package com.dementiaquiz.android.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.ActivityMainBinding
import timber.log.Timber

//TODO: can probably remove this constant, unless when we are loading quiz from file after onStop/onResume in fragment
const val QUIZ_QUESTIONS_JSON = "quiz_questions.json"

/**
 * Main activity for the Dementia Quiz App, hosts a number of fragments
 */
class MainActivity : AppCompatActivity() {

    // Creating a binding object for the main_activity.xml layout
    private lateinit var binding: ActivityMainBinding

    // Initialize the current question index to 0 (the first question)
    // TODO: probably need to store this persistent data variable somewhere else
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.i("onCreate called")

        // use DataBindingUtil to set the content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root
        setContentView(view)

    }
}