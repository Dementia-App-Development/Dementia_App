package com.dementiaquiz.android.fragments

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.dementiaquiz.android.R
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.databinding.FragmentQuizBinding
import com.dementiaquiz.android.models.QuizQuestion
import com.dementiaquiz.android.models.QuizViewModel
import com.dementiaquiz.android.utils.TimeConverter
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


/**
 * Displays the current quiz question and handles user input in answering the question
 */
class QuizFragment : Fragment(), TextToSpeech.OnInitListener {

    // Initialize count down timer
    private var countDown: CountDownTimer? = null

    private lateinit var quizViewModel: QuizViewModel
    private val REQUEST_CODE_SPEECH_INPUT = 100
    private var tts: TextToSpeech? = null
    private var answer: String = "False"
    private var talk: String? = null
    private var voiceAnswer: String = ""
    private lateinit var binding: FragmentQuizBinding
    private val displayMetrics: DisplayMetrics by lazy { Resources.getSystem().displayMetrics }
    private val screenRectPx: Rect
        get() = displayMetrics.run { Rect(0, 0, widthPixels, heightPixels) }

    /**
     * Initialize TTS
     */
    override fun onInit(status: Int) {
        if(status != TextToSpeech.ERROR) {
            tts?.language = Locale.UK
            tts!!.speak(talk, TextToSpeech.QUEUE_FLUSH, null)
        }
    }

    /**
     * Shut down text to speech on fragment destroyed
     */
    override fun onDestroy() {
        // manually destroy the viewModel when the fragment is destroyed
        activity?.viewModelStore?.clear()

        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }


    /**
     * Speech to text response from Google
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    // Get Text from result
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    if (result != null) {
                        voiceAnswer = result[0]
                    }

                    // Return result of question
//                    Timber.i(voiceAnswer)
                }
            }
        }
    }

    /**
     * Initialize text to speech, view model, and handle bindings of UI elements
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Initialize text to speech
        tts = TextToSpeech(activity, this)

        // Inflate the binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quiz, container, false)

        // Get the viewmodel and set to the first question
        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)
//        Timber.i("QUIZ MODE: %s", quizViewModel.mode)
        quizViewModel.setFirstQuestion()

        // Get user ID argument using by navArgs property delegate
        val quizFragmentArgs by navArgs<QuizFragmentArgs>()
        val userID = quizFragmentArgs.userID

        /**
         * Logic and UI handling when a new question is detected in the quiz
         */
        quizViewModel.currentQuestion.observe(viewLifecycleOwner, { newQuestion ->

//            Timber.i("Q" + newQuestion.question_no.toString() + " answers: %s", newQuestion.answers.toString())

            toggleUIQuestionElements(newQuestion, binding)

            // Toggle visibility of respective response parts of the UI based on the question response type
            toggleUIResponseElements(newQuestion, binding, quizViewModel)

            // Set the bindings in the UI of the elements consistent among all questions
            binding.quizQuestionNumTextView.text = " Question " + newQuestion.question_no.toString()
            binding.quizInstructionsTextView.text = newQuestion.instruction
            binding.quizSubTextView.text = newQuestion.sub_text

            // Used for answer handling
            answer = newQuestion.answers.toString()

            // Get the new question instruction and pass it to text to speech
            talk = newQuestion.instruction
            tts!!.speak(newQuestion.instruction, TextToSpeech.QUEUE_FLUSH, null)
        })

        /**
         * Set up LiveData observation relationship to detect for when the quiz has completed
         * Writes the quiz result to db and passes results to post quiz fragment
         */
        quizViewModel.quizIsFinished.observe(viewLifecycleOwner, { newQuizIsFinished ->
            if (newQuizIsFinished == true) {

                // create a copy of the answers. Since the quizAnswerList in viewModel is mutableList,
                // it may change overtime, We will need to make a stable copy of it
                val quizAnswerList = quizViewModel.quizAnswerList.toMutableList()


                // Fetch the current score and convert to double percentage
                val correctNumOfAnswers = quizViewModel.score.value ?: 0
//                Timber.i("correctNumOfAnswers is: $correctNumOfAnswers")
//                Timber.i("question numbers is: ${quizAnswerList.size}")

                val scaledScore = (correctNumOfAnswers.toDouble()/quizAnswerList.size.toDouble()*100).toInt()
                val currentTime = LocalDateTime.now()
                val timeCreated = TimeConverter.convertToDateViaInstant(currentTime)
//                Timber.i("result timeCreated is: $timeCreated")

                // Create quiz result object to be inserted to the database.
                // The ID is set to 0 to be auto-generated
                val quizResult = QuizResult(0, userID, scaledScore, timeCreated)

                // insert the result and the answers to the database,
                quizViewModel.insertQuizResultAndAnswers(quizResult,quizAnswerList).observe(viewLifecycleOwner){ resultId ->

                    if (resultId<=0){
                        // something goes wrong
                        Toast.makeText(context,
                            "Something is wrong, quiz result is not saved",
                            Toast.LENGTH_SHORT).show()
                    }else{
                        // all good
//                        Timber.i("The inserted result ID is: $resultId")
                        val action = QuizFragmentDirections.actionQuizFragmentToPostQuizFragment(userID, resultId, scaledScore)
                        view?.findNavController()?.navigate(action)

                    }
                }

            }
        })

        // Start timer when start timer button is pressed
        binding.quizStartTimerButton.setOnClickListener{
            // clear the old timer if it exist
            countDown?.cancel()

            binding.quizProgressBar.visibility = View.VISIBLE
            binding.quizStartTimerButton.visibility = View.GONE
            binding.quizTrueButton.visibility = View.VISIBLE
            binding.quizFalseButton.visibility = View.VISIBLE
            countDown = quizViewModel.startTimer(binding, 60 * 1000)
        }

        // Repeat the quiz question text to speech when button is pressed
        binding.quizRepeatButton.setOnClickListener{
            tts!!.speak(talk, TextToSpeech.QUEUE_FLUSH, null)
        }

        // Go to the next question
        binding.quizNextButton.setOnClickListener {
            // Cancel countdown and go to next question
            countDown?.cancel()

            // Verify answer against what was input in the edit text response field
            if (binding.quizUserResponseEditText.visibility == View.VISIBLE){
                val response = binding.quizUserResponseEditText.text.toString()
                quizViewModel.onNext(response, answer, false)
            }
            else if (binding.quizDateEditText.visibility == View.VISIBLE) {
                var response = binding.quizDateEditText.text.toString()
                if (response != "") {
                    // reformat the date to keep it consistent with the answers on server
                    // original from user response example: Y-M-D
                    // need to convert to the same format as the one from the server, in this case: dd/mm/yy
                    val dateList = response.split("-")
                    val year = dateList[0].toInt()
                    val month = dateList[1].toInt()
                    val day = dateList[2].toInt()
                    val date = LocalDate.of(year, month, day)
                    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
                    val formattedDate = date.format(formatter)
                    response = formattedDate

//                    Timber.i("The date picked is: $date")
//                    Timber.i("The formatted date picked is: $formattedDate")
                    quizViewModel.onNext(response, answer, false)
                }
                else {
                    quizViewModel.onNext("", answer, false)
                }
            }
            else {
                quizViewModel.onNext(voiceAnswer, answer, false)
            }

            binding.quizUserResponseEditText.text.clear()
            binding.quizDateEditText.text.clear()

        }

        // Assisted mode true/false response buttons
        binding.quizTrueButton.setOnClickListener {
            // Cancel countdown and go to next question
            countDown?.cancel()
            quizViewModel.onNext("", answer, true)
        }
        binding.quizFalseButton.setOnClickListener {
            // Cancel countdown and go to next question
            countDown?.cancel()
            quizViewModel.onNext("", answer, false)
        }

        // Text to speech
        fun speak() {
            // Intent to show SpeechToText dialog
            val mIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            mIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            mIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hi speak something")

            try {
                // If there is no error show SpeechTo Text dialog
                startActivityForResult(mIntent, REQUEST_CODE_SPEECH_INPUT)
            } catch (e: Exception) {
                // If there is any error get error message and show in toast
                //Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
        }

        // Show next question button when voice button is pressed
        binding.quizVoiceButton.setOnClickListener{
            speak()
            binding.quizNextButton.visibility = View.VISIBLE
        }

        // Open a date picker dialog when pressing date picker edit text
        binding.quizDateEditText.setOnClickListener {
            context?.let { showDatePickerDialog(it, binding) }
        }

        return binding.root
    }

    /**
     * Toggles question elements based on the quiz question
     */
    private fun toggleUIQuestionElements(newQuestion : QuizQuestion, binding : FragmentQuizBinding) {

        binding.quizInstructionsTextView.visibility = View.VISIBLE
        binding.quizSubTextView.visibility = View.VISIBLE
        binding.quizTrueButton.visibility = View.GONE
        binding.quizFalseButton.visibility = View.GONE
        binding.quizRepeatButton.visibility = View.GONE
        binding.quizUserResponseEditText.visibility = View.GONE
        binding.quizDateEditText.visibility = View.GONE
        binding.quizStartTimerButton.visibility = View.GONE
        binding.quizVoiceButton.visibility = View.GONE
        binding.quizNextButton.visibility = View.VISIBLE
        binding.quizProgressBar.visibility = View.VISIBLE

        if (!TextUtils.isEmpty(newQuestion.image_url)){
            binding.quizSubTextView.visibility  = View.GONE
            binding.quizQuestionImageView.visibility = View.VISIBLE
            val url = newQuestion.image_url?.split('/')
            Picasso.get().load("https://" + url!![2] +'/' + url[3]).resize(screenRectPx.width(), screenRectPx.height()/3).into( binding.quizQuestionImageView)
        }
        else {
            binding.quizSubTextView.visibility  = View.VISIBLE
            binding.quizQuestionImageView.visibility = View.GONE
        }
    }

    /**
     * UI elements are hidden/shown based on the type of question being displayed
     */
    private fun toggleUIResponseElements(newQuestion : QuizQuestion,
                                         binding : FragmentQuizBinding,
                                         quizViewModel : QuizViewModel) {

        when (newQuestion.response_type) {
            QuizQuestion.ResponseType.DATE -> {
                binding.quizDateEditText.visibility = View.VISIBLE
                countDown = quizViewModel.startTimer(binding, 60 * 1000)
            }
            QuizQuestion.ResponseType.ASSISTED -> {
                binding.quizStartTimerButton.visibility = View.VISIBLE
                binding.quizProgressBar.visibility = View.GONE
                binding.quizNextButton.visibility = View.GONE
            }
            QuizQuestion.ResponseType.SPEECH -> {
                binding.quizSubTextView.visibility = View.GONE
                binding.quizRepeatButton.visibility = View.VISIBLE
                binding.quizInstructionsTextView.visibility = View.GONE
                binding.quizVoiceButton.visibility = View.VISIBLE
                binding.quizNextButton.visibility = View.GONE
                countDown = quizViewModel.startTimer(binding, 60 * 1000)
            }
            else -> {
                binding.quizUserResponseEditText.visibility = View.VISIBLE
                countDown = quizViewModel.startTimer(binding, 60 * 1000)
            }
        }
    }

}

/**
 * Displays the date picker dialog and sets the text field equal to the users input
 */
@RequiresApi(Build.VERSION_CODES.N)
private fun showDatePickerDialog(context : Context, binding : FragmentQuizBinding) {
    val datePickerDialog : DatePickerDialog = DatePickerDialog(context)
    // Default year of birth is set to 1940
    datePickerDialog.updateDate(1940,0,1)
    datePickerDialog.datePicker.touchables[0].performClick()
    datePickerDialog.show()
    datePickerDialog.setOnDateSetListener { _, year, month, day ->
        val dobString = ("$year-${month+1}-$day")
        binding.quizDateEditText.setText(dobString)
    }
}