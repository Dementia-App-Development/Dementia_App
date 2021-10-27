package com.dementiaquiz.android.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentQuizBinding
import com.dementiaquiz.android.models.QuizQuestion
import com.dementiaquiz.android.models.QuizViewModel
import timber.log.Timber
import java.util.*
import android.os.CountDownTimer as CountDownTimer
import android.graphics.drawable.Drawable
import androidx.navigation.findNavController
import java.io.InputStream
import java.net.URL


/**
 * Displays the current quiz question and handles user input in answering the question
 */
class QuizFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var viewModel: QuizViewModel
    private val REQUEST_CODE_SPEECH_INPUT = 100
    private var tts: TextToSpeech? = null
    private var answer: String = "False"
    private var talk: String? = null
    private var voiceAnswer: String = "Nothing"
    private lateinit var binding: FragmentQuizBinding

    /**
     * Create views for the quiz by inflating XML
     */
    override fun onInit(status: Int) {
        if(status != TextToSpeech.ERROR) {
            tts?.language = Locale.UK
        }
    }

    override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }

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
                    Timber.i(voiceAnswer)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initialize text to speech
        tts = TextToSpeech(activity, this)

        // Inflate the binding
        binding = DataBindingUtil.inflate<FragmentQuizBinding>(inflater, R.layout.fragment_quiz, container, false)

        // Get the viewmodel
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)

        // Initialize count down timer
        var countDown: CountDownTimer? = null

        // Set up LiveData observation relationship for the current question in the quiz
        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer<QuizQuestion> { newQuestion ->
            Timber.i(newQuestion.answers.toString())
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
            if (TextUtils.isEmpty(newQuestion.image_url)){
                binding.quizSubTextView.visibility  = View.GONE
                binding.quizQuestionImageView.visibility = View.VISIBLE
                binding.quizQuestionImageView.setImageDrawable(loadImageFromWebOperations(newQuestion.image_url))
            }
            else {
                binding.quizSubTextView.visibility  = View.VISIBLE
                binding.quizQuestionImageView.visibility = View.GONE
            }

            // TODO: Bind the loadingbar https://stackoverflow.com/questions/45373007/progressdialog-is-deprecated-what-is-the-alternate-one-to-use
            // TODO: in some way?

            // Toggle visibility of respective parts of the UI based on the question response type
            when (newQuestion.response_type) {
                QuizQuestion.ResponseType.DATE -> {
                    binding.quizDateEditText.visibility = View.VISIBLE
                    countDown = viewModel.startTimer(binding, 60 * 1000)
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
                    countDown = viewModel.startTimer(binding, 60 * 1000)
                }
                else -> {
                    binding.quizUserResponseEditText.visibility = View.VISIBLE
                    countDown = viewModel.startTimer(binding, 60 * 1000)
                }
            }

            // Set the bindings in the UI to default values
            binding.quizQuestionNumTextView.text = " Question " + newQuestion.question_no.toString()
            binding.quizInstructionsTextView.text = newQuestion.instruction
            binding.quizSubTextView.text = newQuestion.sub_text

            // TODO: old code, can delete this I think
            // No this do not delete this currently is used for answer handling
            answer = newQuestion.answers.toString()

            // Get the new question instruction and pass it to text to speech
            talk = newQuestion.instruction
            tts!!.speak(newQuestion.instruction, TextToSpeech.QUEUE_FLUSH, null)
        })

        // Set up LiveData observation relationship to detect for when the quiz has completed
        viewModel.quizIsFinished.observe(viewLifecycleOwner, Observer<Boolean> { newBoolean ->
            if (newBoolean == true) {

                // Fetch the current score
                val currentScore = viewModel.score.value ?: 0

                // Finish the quiz with the current score bundle passed to the post quiz fragment
                val action = QuizFragmentDirections.actionQuizFragmentToPostQuizFragment(currentScore)
                view?.findNavController()?.navigate(action)
            }
        })

        // Start timer when start timer button is pressed
        binding.quizStartTimerButton.setOnClickListener{
            binding.quizProgressBar.visibility = View.VISIBLE
            binding.quizStartTimerButton.visibility = View.GONE
            binding.quizTrueButton.visibility = View.VISIBLE
            binding.quizFalseButton.visibility = View.VISIBLE
            countDown = viewModel.startTimer(binding, 60 * 1000)
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
            // TODO: implement this properly, put in when/case loop
            if (binding.quizUserResponseEditText.visibility == View.VISIBLE){
                val response = binding.quizUserResponseEditText.text.toString()
                viewModel.onNext(response, answer, false)
            }
            else if (binding.quizDateEditText.visibility == View.VISIBLE) {
                val response = binding.quizDateEditText.text.toString()
                viewModel.onNext(response, answer, false)
            }
            else {
                viewModel.onNext(voiceAnswer, answer, false)
            }
        }

        // Assisted mode true/false response buttons
        binding.quizTrueButton.setOnClickListener {

            // Verify answer against what was input in the edit text response field

            // Cancel countdown and go to next question
            countDown?.cancel()
            viewModel.onNext(null, answer, true)
        }
        binding.quizFalseButton.setOnClickListener {

            // Verify answer against what was input in the edit text response field
            // Cancel countdown and go to next question
            countDown?.cancel()
            viewModel.onNext(null, answer, false)
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

        // Go to the previous question
        //binding.btnPrevious.setOnClickListener {
        //    viewModel.onPrev()
        //}

        return binding.root
    }

}

private fun loadImageFromWebOperations(url: String?): Drawable? {
    return try {
        val `is`: InputStream = URL(url).content as InputStream
        Drawable.createFromStream(`is`, "src name")
    } catch (e: java.lang.Exception) {
        null
    }
}