package com.dementiaquiz.android.models

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.CountDownTimer
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

/**
 * Holds the information for the current question in the quiz
 */
class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val quizResultRepository: QuizResultRepository =
        getApplication<DementiaQuizApplication>().quizResultRepository

    private val REQUEST_CODE_SPEECH_INPUT = 100

    // A List of quiz question objects
    private var quizQuestions: List<QuizQuestion>

    // The index of the current question in the quiz question list
    private var currentQuestionIndex: Int

    // The list of answers for the quiz
    var quizAnswerList : MutableList<QuizAnswer> = mutableListOf()

    // The result of the quiz
    lateinit var quizResult : QuizResult

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

    // Boolean set to true if the quiz is able to fetch GPS coordinates
    private var _gpsCoordinatesFetched = MutableLiveData<Boolean>()
    val gpsCoordinatesFetched: LiveData<Boolean>
        get() = _gpsCoordinatesFetched

    // Boolean that is true/false depending on whether permissions have been granted by the user
    private var _gpsPermissionsGranted = MutableLiveData<Boolean>()
    val gpsPermissionsGranted: LiveData<Boolean>
        get() = _gpsPermissionsGranted

    // Application context, used for location access
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    // Location client
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    private val locationListener = LocationListener { location ->
        myLat = location.latitude
        myLong = location.longitude
//        Timber.i("myLatNew= %s | myLongNew= %s", myLat.toString(), myLong.toString())

        _gpsCoordinatesFetched.value = true
    }

    private val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    // Quiz latitude, longitude and mode
    private var myLat: Double? = null
    private var myLong: Double? = null
    private var mode: String = ""


    /**
     * Generate the quiz question list and set the current question to the first in the list
     */
    init {
        quizQuestions = emptyList()
        currentQuestionIndex = 0
        _score.value = 0
        _quizIsLoading.value = false
        _gpsPermissionsGranted.value = false
    }

    /**
     * Polls for location
     */
    fun prePollLocation(context: Context) {
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Location is not granted permission
            _gpsPermissionsGranted.value = false

//            Timber.i("GPS not granted permission")

            return
        } else {
            // GPS has been granted permission
            _gpsPermissionsGranted.value = true
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
            0.01.toFloat(), locationListener)
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
            0.01.toFloat(), locationListener)
    }

    fun stopPollLocation(){
        locationManager.removeUpdates(locationListener)
    }

    // Get the location of the device
    private fun getLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        context: Context
    ) {
//        Timber.i("Fetching device location")

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Location is not granted permission
            _gpsPermissionsGranted.value = false

//            Timber.i("GPS not granted permission")

            return
        } else {
            // GPS has been granted permission
            _gpsPermissionsGranted.value = true
        }

        // Add the on success and on failure methods for GPS request
        fusedLocationProviderClient.lastLocation.apply {
            addOnFailureListener {
                // Handle if GPS location request failure
                _gpsCoordinatesFetched.value = false

//                Timber.i("GPS location request failure")

            }
            addOnSuccessListener {

                _gpsCoordinatesFetched.value = true

                // Get last known location if it is not null, then get all quiz questions
                if (it != null) {
                    //You can extract the details from the client, like the latitude and longitude of the place and use it accordingly.
                    myLat = it.latitude
                    myLong = it.longitude
//                    Timber.i("myLat= %s | myLong= %s", myLat.toString(), myLong.toString())

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
        // fetch the quiz everytime the mode button is clicked
        mode = newMode
        getLocation(fusedLocationClient, context)


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
            QuizApi.retrofitService.getAllCustomQuestionsNoGPS(mode)
                .enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        _response.value = "Failure: " + t.message
//                        Timber.i("API failure")
                    }

                    // Get the quiz questions from API call and sort them
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        _response.value = response.body()
//                        Timber.i("API response:")
//                        Timber.i(response.body())

                        // Parse the json response to generate quiz question list
                        quizQuestions = response.body()?.let { generateQuizQuestionsFromJson(it) }!!
//                        Timber.i("Retrieved quiz questions from API")
//                        Timber.i("unsorted questions: " + quizQuestions)


                        // Sort the questions list by id
                        quizQuestions = quizQuestions.sortedWith(compareBy { it.question_no })
//                        Timber.i("sorted questions: " + quizQuestions)

//                        Timber.i("Length of quiz= %s", quizQuestions.size)

                        // Set the first question
                        currentQuestionIndex = 0
                        _currentQuestion.value = quizQuestions[currentQuestionIndex]
                        // As the quiz is now loaded, set the quiz is loading now to false
                        _quizIsLoading.value = false

                    }
                })

            // Fetch all quiz questions with GPS coordinated provided
        } else {
            // Fetch quiz questions from API call
            QuizApi.retrofitService.getAllCustomQuestions(myLat.toString(), myLong.toString(), mode)
                .enqueue(object : Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        _response.value = "Failure: " + t.message
//                        Timber.i("API failure")
                    }

                    // Get the quiz questions from API call and sort them
                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        _response.value = response.body()
//                        Timber.i("API response:")
//                        Timber.i(response.body())

                        // Parse the json response to generate quiz question list
                        quizQuestions = response.body()?.let { generateQuizQuestionsFromJson(it) }!!
//                        Timber.i("Retrieved quiz questions from API")
//                        Timber.i("unsorted questions: " + quizQuestions)


                        // Sort the questions list by id
                        quizQuestions = quizQuestions.sortedWith(compareBy { it.question_no })
//                        Timber.i("sorted questions: " + quizQuestions)

//                        Timber.i("Length of quiz= %s", quizQuestions.size)

                        // Set the first question
                        currentQuestionIndex = 0
                        _currentQuestion.value = quizQuestions[currentQuestionIndex]

                        // As the quiz is now loaded, set the quiz is loading now to false
                        _quizIsLoading.value = false
                    }
                })
        }
    }

    /**
     * Checks the user answer against the true answer, returns boolean
     */
    private fun isResponseCorrect(userAnswer: String?, trueAnswer: String, assistedCorrect: Boolean) : Boolean {

        // make the answers correctness case insensitive
        val userAnswerUppercase = userAnswer?.uppercase()
        val trueAnswerUppercase = trueAnswer.uppercase()

        // Check if the answer provided is correct
        return if (userAnswerUppercase == "" && !assistedCorrect) {
    //            Timber.i("Question #" + currentQuestion.value?.question_no.toString() + " Answer is X wrong X")
            false
        } else if ( assistedCorrect || trueAnswerUppercase.contains(userAnswerUppercase!!)){
    //            Timber.i("Question #" + currentQuestion.value?.question_no.toString() + " Answer is ✔ correct ✔")
            // Increment the score
            _score.value = (_score.value)?.plus(1)
            true
        } else {
    //            Timber.i("Question #" + currentQuestion.value?.question_no.toString() + " Answer is X wrong X")
            false
        }
    }

    /**
     * Input is the user response and true answer to the question, returns a quiz answer object
     */
    private fun getQuestionAnswer(userAnswer: String?, trueAnswer: String, assistedCorrect: Boolean) : QuizAnswer {

        // Generate a QuizAnswer object
        val questionDescription = quizQuestions[currentQuestionIndex].instruction
        // Converts list of answers to a concatenated list
        val correctAnswer = quizQuestions[currentQuestionIndex].answers?.joinToString("&") ?: ""
        val response : String = userAnswer.toString()
        val correct = isResponseCorrect(userAnswer, trueAnswer, assistedCorrect)
        val resultId = 0L
        return QuizAnswer(0, questionDescription, correctAnswer, response, correct, resultId)
    }

    /**
     * Goes to the next question, also checks the answer and records the result
     */
    fun onNext(userAnswer: String?, trueAnswer: String, assistedCorrect: Boolean) {

        // Get a quizAnswer object and append to quiz answer list
        val quizAnswer = getQuestionAnswer(userAnswer, trueAnswer, assistedCorrect)
        quizAnswerList.add(quizAnswer)

        // Check whether at the end of the quiz
        if (currentQuestionIndex < quizQuestions.size - 1) {
            currentQuestionIndex += 1
            _currentQuestion.value = quizQuestions[currentQuestionIndex]
        } else {
            _quizIsFinished.value = true
            quizAnswerList.clear()
        }

    }

    /**
     * Starts the countdown timer
     */
    fun startTimer(binding: FragmentQuizBinding, Min: Int): CountDownTimer {
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


    // insertQuizResult and answers belong to that result. It returns the LiveData that represent ID of
    // the inserted QuizResult
    fun insertQuizResultAndAnswers(quizResult: QuizResult, quizAnswerList:List<QuizAnswer>):LiveData<Long>{

//        Timber.i("QuizResult is: $quizResult")
//        Timber.i("quizAnswerList is: $quizAnswerList")

        val quizResultId =MutableLiveData<Long>()

        viewModelScope.launch {
            val num = quizResultRepository.insertQuizResultAndAnswers(quizResult,quizAnswerList)
            quizResultId.postValue(num)
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
//        Timber.i("Could not generate quiz questions list from json string")
        throw Exception("Could not generate quiz questions list from json string")
    }
}