package com.dementiaquiz.android.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dementiaquiz.android.network.QuizApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

/**
 * Holds the information for the current question in the quiz
 */
class QuizViewModel : ViewModel() {

    private lateinit var quizQuestions: List<QuizQuestion>

    private var currentQuestionIndex: Int

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
        getAllQuizQuestionsAPI()
        Timber.i("getAllQuizQuestionsAPI has been called")

        _currentQuestion.value = quizQuestions[0]
        currentQuestionIndex = 0
    }

    // TODO: API code, to work on later
    private fun getAllQuizQuestionsAPI() {
        Timber.i("Fetching API service...")

        QuizApi.retrofitService.getAllQuestions().enqueue(object : Callback<String> {

            override fun onFailure(call: Call<String>, t: Throwable) {
                Timber.i("onFailure called")

                _response.value = "Failure: " + t.message
                Timber.i("Could not fetch quiz questions")
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                Timber.i("onResponse called")

                _response.value = response.body()
                Timber.i("Fetched quiz questions")

                quizQuestions = generateQuizQuestionsFromJson(_response.value)
                Timber.i("Quiz questions parsed from JSON into list")
            }
        })
    }

    private fun getAllQuizQuestionsPlaceholder() {
        // TODO: Below code generates placeholder two-member list, need to replace so that it fetches data from server
        val questionOne = QuizQuestion(
            1,
            1,
            "What year is this?",
            null,
            10,
            1,
            QuizQuestion.ResponseType.LIST,
            QuizQuestion.AnswerVerification.LIST,
            listOf("2021", "2022", "2023", "2024"),
            null,
            null,
            'a',
            listOf("2021")
        )
        val questionTwo = QuizQuestion(
            2,
            1,
            "What season is this?",
            null,
            10,
            1,
            QuizQuestion.ResponseType.LIST,
            QuizQuestion.AnswerVerification.LIST,
            listOf("Spring", "Autumn", "Summer", "Winter"),
            null,
            null,
            'b',
            listOf("Spring")
        )
        quizQuestions = listOf(questionOne, questionTwo)

        // Sort the questions list by id
        quizQuestions.sortedBy { it.id }
        // TODO: end of placeholder code
    }

    // Go to next question
    fun onNext() {
        if (currentQuestionIndex < quizQuestions.size - 1) {
            currentQuestionIndex += 1
        }
        _currentQuestion.value = quizQuestions[currentQuestionIndex]
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
fun generateQuizQuestionsFromJson(jsonString: String?): List<QuizQuestion> {
    val gson = Gson()
    val quizQuestion = object : TypeToken<List<QuizQuestion>>() {}.type
    return gson.fromJson(jsonString, quizQuestion)
}