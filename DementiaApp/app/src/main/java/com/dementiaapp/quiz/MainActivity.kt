package com.dementiaapp.quiz

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dementiaapp.quiz.databinding.ActivityMainBinding

const val QUIZ_QUESTIONS_JSON = "quiz_questions.json"

class MainActivity : AppCompatActivity() {

    // Creating a binding object for the main_activity.xml layout
    private lateinit var binding: ActivityMainBinding

    // Initialize the current question index to 0 (the first question)
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // use DataBindingUtil to set the content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root
        setContentView(view)

    }
}