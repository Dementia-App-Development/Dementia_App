package com.dementiaquiz.android.models

import android.content.Intent
import android.os.CountDownTimer
import android.speech.RecognizerIntent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dementiaquiz.android.QuizApi
import com.dementiaquiz.android.databinding.FragmentQuizBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Holds the information for the current question in the quiz
 */
class QuizViewModel : ViewModel() {

    private lateinit var quizQuestions : List<QuizQuestion>
    private var currentQuestionIndex: Int
    private val REQUEST_CODE_SPEECH_INPUT = 100;
    // The current question
    private var _currentQuestion = MutableLiveData<QuizQuestion>()
    val currentQuestion: LiveData<QuizQuestion>
        get() = _currentQuestion

    // The internal MutableLiveData String that stores the status of the most recent request
    //TODO: used for API respone - yet to implement
    private val _response = MutableLiveData<String>()
    val response: LiveData<String> // The external immutable LiveData for the request status String
        get() = _response

    /**
     * Generate the quiz question list and set the current question to the first in the list
     */
    init {
        quizQuestions = getAllQuizQuestions()
        _currentQuestion.value = quizQuestions[0]
        currentQuestionIndex = 0
    }

    private fun getAllQuizQuestions(): List<QuizQuestion> {

        // TODO: Below code generates placeholder two-member list, need to replace so that it fetches data from server
        val questionOne = QuizQuestion(
            1,
            1,
            "What year is this?",
            null,
            10,
            1,
            QuizQuestion.ResponseType.DATE,
            QuizQuestion.AnswerVerification.LIST,
            null,
            null,
            'a',
            listOf("2021"),
            answers = null
        )
        val questionOneb = QuizQuestion(
            2,
            1,
            "What year is this?",
            null,
            10,
            1,
            QuizQuestion.ResponseType.ASSISTED,
            QuizQuestion.AnswerVerification.LIST,
            null,
            null,
            'a',
            listOf("2021"),
            answers = null
        )
        val questionTwo = QuizQuestion(
            3,
            1,
            "What season is this?",
            null,
            10,
            1,
            QuizQuestion.ResponseType.TEXT,
            QuizQuestion.AnswerVerification.LIST,
            null,
            null,
            'b',
            listOf("Spring"),
            answers = null
        )
        val questionTwob = QuizQuestion(
            4,
            1,
            "What season is this?",
            null,
            10,
            1,
            QuizQuestion.ResponseType.ASSISTED,
            QuizQuestion.AnswerVerification.LIST,
            null,
            null,
            'b',
            listOf("Spring"),
            answers = null
        )
        val questionThree = QuizQuestion(
            5,
            1,
            "Please the following phrase: I like quizes",
            "Click on the microphone when you are ready",
            10,
            1,
            QuizQuestion.ResponseType.SPEECH,
            QuizQuestion.AnswerVerification.LIST,
            null,
            null,
            'b',
            listOf("Spring"),
            answers = null
        )
        val quizQuestions = listOf(questionOne, questionOneb, questionTwo, questionTwob, questionThree)

        // Sort the questions list by id
        quizQuestions.sortedBy { it.id }

        return quizQuestions
        // TODO: end of placeholder code

        // TODO: API code, to work on later
//        QuizApi.retrofitService.getAllQuizQuestions().enqueue( object: Callback<String> {
//            override fun onFailure(call: Call<String>, t: Throwable) {
//                _response.value = "Failure: " + t.message
//            }
//
//            override fun onResponse(call: Call<String>, response: Response<String>) {
//                _response.value = response.body()
//            }
//        })
    }

    // Go to next question
    fun onNext() {
        if (currentQuestionIndex < quizQuestions.size - 1) {
            currentQuestionIndex += 1
        }

        _currentQuestion.value = quizQuestions[currentQuestionIndex]
    }
    fun startTimer(binding: FragmentQuizBinding, Min: Int): CountDownTimer {
        binding.bar.progress = 0
        val MyCountDownTimer = object : CountDownTimer(Min.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val fraction = millisUntilFinished / Min.toDouble()
                binding.bar.progress = (fraction * 100).toInt()
            }

            override fun onFinish() {
                binding.bar.progress = 0
                onNext()
            }
        }
        MyCountDownTimer.start()
        return MyCountDownTimer
    }
    // Go to previous question
    fun onPrev() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex -= 1
        }
        _currentQuestion.value = quizQuestions[currentQuestionIndex]
    }
}