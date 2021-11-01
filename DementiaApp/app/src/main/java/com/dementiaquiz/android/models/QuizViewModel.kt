package com.dementiaquiz.android.models

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.CountDownTimer
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.WorkerThread
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.*
import androidx.room.Transaction
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.QuizApi
import com.dementiaquiz.android.database.model.QuizAnswer
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.databinding.FragmentQuizBinding
import com.dementiaquiz.android.repositories.QuizResultRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.util.*

/**
 * Holds the information for the current question in the quiz
 */
class QuizViewModel(application : Application) : AndroidViewModel(application) {

    private val quizResultRepository:QuizResultRepository = getApplication<DementiaQuizApplication>().quizResultRepository

    // TODO: variable never used, can delete?
    private val REQUEST_CODE_SPEECH_INPUT = 100;

    // A List of quiz question objects
    private var quizQuestions : List<QuizQuestion>

    // The index of the current question in the quiz question list
    private var currentQuestionIndex : Int

    // A tally of how many correct answers the user has gotten in the quiz
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

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

    // Used to determine whether quiz is currently loading, and to show/hide go to quiz button in pre quiz
    private var _quizIsLoading = MutableLiveData<Boolean>()
    val quizIsLoading: LiveData<Boolean>
        get() = _quizIsLoading

    // Application context, used for location access
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    // Location client
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // Quiz latitude, longitude and mode
    var myLat : Double? = null
    var myLong : Double? = null
    var mode : String = ""


    /**
     * Generate the quiz question list and set the current question to the first in the list
     */
    init {
        quizQuestions = emptyList()
        currentQuestionIndex = 0
        _score.value = 0
    }

    // Get the location of the device
    private fun getLocation(fusedLocationProviderClient : FusedLocationProviderClient, context: Context) {
        Timber.i("Fetching device location")

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // TODO: handle if location is no granted permission
            return
        }

        fusedLocationProviderClient.lastLocation.apply {
            addOnFailureListener{
                //Handle the failure of the call. You can show an error dialogue or a toast stating the failure in receiving location.

                Timber.i("GPS location request failure")
            }
            addOnSuccessListener {
                // Get last known location if it is not null, then get all quiz questions
                if (it != null) {
                    //You ca extract the details from the client, like the latitude and longitude of the place and use it accordingly.
                    myLat = it.latitude
                    myLong = it.longitude
                    Timber.i("myLat= %s", myLat.toString())
                    Timber.i("myLong= %s", myLong.toString())

                    getAllQuizQuestions()
                }
            }
        }
    }

    /**
     * Sets the mode of the quiz to the input, one of either "solo", "assisted-home" or "assisted-clinic"
     * Then gets the location of the quiz
     */
    fun setQuizMode(newMode: String) {
        // Fetch quiz if the new mode is different to the current mode
        if (newMode != mode) {
            mode = newMode
            getLocation(fusedLocationClient, context)
        }
    }

    /**
     * Sets the first question of the quiz so long as it is loaded (not empty)
     */
    fun setFirstQuestion() {
        if (quizQuestions.isNotEmpty()) {
            _currentQuestion.value = quizQuestions[currentQuestionIndex]
        }
    }

    /**
     * Gets all quiz questions from server, calls one of two API methods depending on whether location provided
     */
    private fun getAllQuizQuestions() {
        // As the quiz is loading, set the quiz is loading to true
        _quizIsLoading.value = true

        // If no GPS can be fetched, send API request with no latitude and longitude values
        if (myLat == null || myLong == null) {
            QuizApi.retrofitService.getAllCustomQuestionsNoGPS(mode).enqueue( object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = "Failure: " + t.message
                    Timber.i("API failure")

                    // TODO: handle API response failure exception with dialog prompt
                }

                // Get the quiz questions from API call and sort them
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _response.value = response.body()
                    Timber.i("API response:")
                    Timber.i(response.body())

                    // Parse the json response to generate quiz question list
                    quizQuestions = response.body()?.let { generateQuizQuestionsFromJson(it) }!!
                    Timber.i("Retrieved quiz questions from API")

                    // As the quiz is now loaded, set the quiz is loading now to false
                    _quizIsLoading.value = false

                    // Sort the questions list by id
                    quizQuestions.sortedBy { it.id }
                    Timber.i("Length of quiz= %s", quizQuestions.size)

                    // Set the first question
                    currentQuestionIndex = 0
                    _currentQuestion.value = quizQuestions[currentQuestionIndex]
                }
            })

            // Fetch all quiz questions with GPS coordinated provided
        } else {
            // Fetch quiz questions from API call
            QuizApi.retrofitService.getAllCustomQuestions(myLat.toString(), myLong.toString(), mode).enqueue( object: Callback<String> {
                override fun onFailure(call: Call<String>, t: Throwable) {
                    _response.value = "Failure: " + t.message
                    Timber.i("API failure")

                    // TODO: handle API response failure exception with dialog prompt
                }

                // Get the quiz questions from API call and sort them
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    _response.value = response.body()
                    Timber.i("API response:")
                    Timber.i(response.body())

                    // Parse the json response to generate quiz question list
                    quizQuestions = response.body()?.let { generateQuizQuestionsFromJson(it) }!!
                    Timber.i("Retrieved quiz questions from API")

                    // As the quiz is now loaded, set the quiz is loading now to false
                    _quizIsLoading.value = false

                    // Sort the questions list by id
                    quizQuestions.sortedBy { it.id }
                    Timber.i("Length of quiz= %s", quizQuestions.size)

                    // Set the first question
                    currentQuestionIndex = 0
                    _currentQuestion.value = quizQuestions[currentQuestionIndex]
                }
            })
        }
    }

    /**
     * Goes to the next question, also checks the answer and records the result
     */
    fun onNext(userAnswer: String?, trueAnswer: String, assistedCorrect: Boolean) {
        // TODO: Check if the answer provided is correct
        if (userAnswer == null && !assistedCorrect) {
            Timber.i("Wrong!")
        }
        else if ( assistedCorrect || trueAnswer.contains(userAnswer!!)){
            Timber.i("Well Done")

            // Increment the score
            _score.value = (_score.value)?.plus(1)
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
            // TODO: make this a method that, before setting game finished to true, saves the results of the quiz to database
            _quizIsFinished.value = false
            currentQuestionIndex = 0
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
                binding.quizNextButton.performClick()
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

    // insertQuizResult and answers belong to that result. It returns the ID of
    // the inserted QuizResult
    fun insertQuizResultAndAnswers(quizResult: QuizResult, quizAnswerList:List<QuizAnswer>):Long{

        var quizResultId:Long =-1;
        viewModelScope.launch {
            quizResultId = quizResultRepository.insertQuizResultAndAnswers(quizResult,quizAnswerList)
        }
        return quizResultId

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