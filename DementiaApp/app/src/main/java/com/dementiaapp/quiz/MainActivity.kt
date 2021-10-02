package com.dementiaapp.quiz

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dementiaapp.quiz.databinding.ActivityMainBinding
import android.os.CountDownTimer




const val QUIZ_QUESTIONS_JSON = "quiz_questions.json"

class MainActivity : AppCompatActivity() {

    // Creating a binding object for the main_activity.xml layout
    private lateinit var binding: ActivityMainBinding

    // Initialize the current question index to 0 (the first question)
    private var currentQuestionIndex = 0
    val Min = 60 * 1000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // use DataBindingUtil to set the content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root
        setContentView(view)
        // Parse the quiz questions json from file and create quiz questions kotlin object
        val jsonFileString = getJsonDataFromAsset(applicationContext, QUIZ_QUESTIONS_JSON)
        var quizQuestions = generateQuizQuestions(jsonFileString)
        Log.i("quiz data", quizQuestions.toString())

        // Sort the questions list by id
        quizQuestions = quizQuestions.sortedBy {it.id}

        // Initialize the question fields in the UI
        populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
        Log.i("Test", "Is this working")
        startTimer(binding, Min)


        /** CountDownTimer starts with 1 minute and every onTick is 1 second */

        // When next button is pressed, go to next question so long as not at end of question list
        binding.nextBtn.setOnClickListener {
            if (currentQuestionIndex < quizQuestions.size - 1) {
                currentQuestionIndex += 1
                populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
            }
        }

        // When prev button is pressed, go to prev question so long as not at start of question list
        //binding.prevBtn.setOnClickListener {
        //    if (currentQuestionIndex > 0) {
        //        currentQuestionIndex -= 1
        //        populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
        //    }
        // }

    }

}

private fun startTimer(binding: ActivityMainBinding, Min: Int) {
    Log.i("Timer", "Is this working")
    binding.bar.progress = 10
    val MyCountDownTimer = object : CountDownTimer(Min.toLong(), 1000) {
        override fun onTick(millisUntilFinished: Long) {
            val secondsRemaining = millisUntilFinished.toInt() / 1000
            val fraction = (millisUntilFinished / Min).toFloat()
            binding.bar.progress = (fraction * 100).toInt()
        }

        override fun onFinish() {
            // DO something when 2 minutes is up
        }
    }
    MyCountDownTimer.start()
}
/**
 * This function populates the UI with the question at the provided index in the list of questions
 */
// TODO: temporary code for demonstration purposes only
fun populateUIWithQuestion(binding: ActivityMainBinding, quizQuestions: List<QuizQuestion>, i: Int) {
    // Instantiate the fields in the main activity with the first question in the quiz
    // binding.tvTimeLimit.text = quizQuestions[i].time_limit.toString() + " seconds to answer"
    if (quizQuestions[i].id == 4) {

    } else {

    }
    binding.tvQuestionNo.text = " Question " + quizQuestions[i].question_no.toString() +
            quizQuestions[i].sub_question.toString()
    binding.tvInstructions.text = quizQuestions[i].instruction
    binding.tvSubText.text = quizQuestions[i].sub_text
}