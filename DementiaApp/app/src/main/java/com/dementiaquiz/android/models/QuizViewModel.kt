package com.dementiaquiz.android.models

import android.content.Intent
import android.os.CountDownTimer
import android.speech.RecognizerIntent
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dementiaquiz.android.QuizApi
import com.dementiaquiz.android.database.model.QuizAnswer
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

    // TODO: variable never used, can delete?
    private val REQUEST_CODE_SPEECH_INPUT = 100;

    // A List of quiz question objects
    private var quizQuestions : List<QuizQuestion>

    // The index of the current question in the quiz question list
    private var currentQuestionIndex : Int

    // A tally of how many correct answers the user has gotten in the quiz
    private var correctAnswersTally : Int = 0

    // Boolean value that defaults to false, and is set to true when the quiz is finished
    private var _quizIsFinished = MutableLiveData<Boolean>()
    val quizIsFinished: LiveData<Boolean>
        get() = _quizIsFinished

    // The current question to be displayed in the UI
    private var _currentQuestion = MutableLiveData<QuizQuestion>()
    val currentQuestion: LiveData<QuizQuestion>
        get() = _currentQuestion

    // The internal MutableLiveData String that stores the status of the most recent request
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

                // TODO: handle API response failure exception with dialog prompt

                // TODO : not sure we should be calling this method here on failure
                getAllQuizQuestions()
            }

            // Get the quiz questions from API call and sort them
            override fun onResponse(call: Call<String>, response: Response<String>) {
                _response.value = response.body()
                Timber.i("API response")
                // Parse the json response to generate quiz question list
                quizQuestions = response.body()?.let { generateQuizQuestionsFromJson(it) }!!
                Timber.i("Retrieved quiz questions from API")

                // Sort the questions list by id
                quizQuestions.sortedBy { it.id }
                Timber.i("Length of quiz= %s", quizQuestions.size)

                // Set the current question to the first question
                _currentQuestion.value = quizQuestions[currentQuestionIndex]
            }
        })

        // TODO: Create a loaded variable that changes to true when everything has been loaded
        // TODO: put it on the onResponse function maybe?
    }

    // Go to next question
    fun onNext(userAnswer: String?, trueAnswer: String?, assistedCorrect: Boolean) {
        // TODO: Check if the answer provided is correct
        if (trueAnswer!!.contains(userAnswer!!) or assistedCorrect){
            Timber.i("Well Done")
            // If string == answer do something
        }
        else {
            Timber.i("Wrong!")
        }

        // Check whether at the end of the quiz
        if (currentQuestionIndex < quizQuestions.size - 1) {
            currentQuestionIndex += 1
        // When at the end of the quiz, set the boolean to true so to move to the post quiz fragment
        } else {
            _quizIsFinished.value = true
        }

        _currentQuestion.value = quizQuestions[currentQuestionIndex]
    }

    fun startTimer(binding: FragmentQuizBinding, Min: Int): CountDownTimer {
        binding.quizProgressBar.progress = 0
        val myCountDownTimer = object : CountDownTimer(Min.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {

                val fraction = millisUntilFinished / Min.toDouble()
                binding.quizProgressBar.progress = (fraction * 100).toInt()
            }

            override fun onFinish() {
                binding.quizProgressBar.progress = 0
                onNext(null, null, false)
            }
        }
        myCountDownTimer.start()
        return myCountDownTimer
    }

    // Go to previous question
    // TODO: I believe this is redundant now and can be removed
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
private fun generateQuizQuestionsFromJson(jsonString: String): List<QuizQuestion> {
    try {
        val gson = Gson()
        val quizQuestion = object : TypeToken<List<QuizQuestion>>() {}.type
        return gson.fromJson(jsonString, quizQuestion)
    } catch (e: Exception) {
        Timber.i("Could not generate quiz questions list from json string")
        throw Exception("Could not generate quiz questions list from json string")
    }
}