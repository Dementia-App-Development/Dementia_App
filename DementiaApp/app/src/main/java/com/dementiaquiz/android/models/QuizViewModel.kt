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
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

/**
 * Holds the information for the current question in the quiz
 */
class QuizViewModel : ViewModel() {

    private var quizQuestions : List<QuizQuestion>
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
        quizQuestions = emptyList()
        currentQuestionIndex = 0
        getAllQuizQuestions()
    }

    private fun getAllQuizQuestions() {
        // Fetch quiz questions from API call
        QuizApi.retrofitService.getAllQuestions().enqueue( object: Callback<String> {
            override fun onFailure(call: Call<String>, t: Throwable) {
                _response.value = "Failure: " + t.message
                Timber.i("API failure")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                _response.value = response.body()
                Timber.i("API response")

                // Parse the json response to generate quiz question list
                quizQuestions = response.body()?.let { generateQuizQuestionsFromJson(it) }!!

                // Sort the questions list by id
                quizQuestions.sortedBy { it.id }

                // Set the current question to the first question
                _currentQuestion.value = quizQuestions[currentQuestionIndex]
            }
        })
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
        val myCountDownTimer = object : CountDownTimer(Min.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val fraction = millisUntilFinished / Min.toDouble()
                binding.bar.progress = (fraction * 100).toInt()
            }

            override fun onFinish() {
                binding.bar.progress = 0
                onNext()
            }
        }
        myCountDownTimer.start()
        return myCountDownTimer
    }

    // Go to previous question
    fun onPrev() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex -= 1
        }
        _currentQuestion.value = quizQuestions[currentQuestionIndex]
    }
}

/**
 * Parses a json string and outputs a list of QuizQuestion objects
 */
fun generateQuizQuestionsFromJson(jsonString: String): List<QuizQuestion> {
    try {
        val gson = Gson()
        val quizQuestion = object : TypeToken<List<QuizQuestion>>() {}.type
        return gson.fromJson(jsonString, quizQuestion)
    } catch (e: Exception) {
        Timber.i("Could not generate quiz questions list from json string")
        throw Exception("Could not generate quiz questions list from json string")
    }
}