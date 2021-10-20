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
import android.opengl.Visibility
import android.util.Log
import java.io.InputStream
import java.net.URL


/**
 * Displays the current quiz question and handles user input in answering the question
 */
class QuizFragment : Fragment(), TextToSpeech.OnInitListener {

    private lateinit var viewModel: QuizViewModel
    private val REQUEST_CODE_SPEECH_INPUT = 100
    private var tts: TextToSpeech? = null
    private var answer: String? = null
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

                    val voice = result?.get(0)
                    // Return result of question
                    Timber.i(voice)
                    if (voice contentEquals answer) {
                        Timber.i("Well Done")
                    }
                    //    textViewAnswerResult.text = "Correct"
                    //} else {
                    //    textViewAnswerResult.text = "Incorrect"
                    //}
                }
            }
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tts = TextToSpeech(activity, this)
        Timber.i("onCreateView called")

        // Inflate the binding
        binding = DataBindingUtil.inflate<FragmentQuizBinding>(
            inflater,
            R.layout.fragment_quiz,
            container,
            false
        )

        // Get the viewmodel
        viewModel = ViewModelProvider(this).get(QuizViewModel::class.java)
        var CountDown: CountDownTimer? = null
        // Set up LiveData observation relationships
        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer<QuizQuestion> { newQuestion ->
            Timber.i(newQuestion.answers.toString() )
            binding.tvInstructions.visibility = View.VISIBLE
            binding.trueButton.visibility = View.GONE
            binding.falseButton.visibility = View.GONE
            if (TextUtils.isEmpty(newQuestion.image_url)){
                binding.tvSubText.visibility  = View.GONE
                binding.imageView.visibility = View.VISIBLE
                binding.imageView.setImageDrawable(LoadImageFromWebOperations(newQuestion.image_url))
            }
            else {
                binding.tvSubText.visibility  = View.VISIBLE
                binding.imageView.visibility = View.GONE
            }
            if (newQuestion.response_type == QuizQuestion.ResponseType.DATE) {
                binding.Text.visibility = View.GONE
                binding.date.visibility = View.VISIBLE
                binding.button.visibility = View.GONE
                binding.voiceButton.visibility = View.GONE
                CountDown = viewModel.startTimer(binding, 60 * 1000)
            }
            else if (newQuestion.response_type == QuizQuestion.ResponseType.ASSISTED) {
                binding.Text.visibility = View.GONE
                binding.date.visibility = View.GONE
                binding.button.visibility = View.VISIBLE
                binding.bar.visibility = View.GONE
                binding.nextBtn.visibility = View.GONE
                binding.voiceButton.visibility = View.GONE
            }
            else if (newQuestion.response_type == QuizQuestion.ResponseType.SPEECH) {
                binding.Text.visibility = View.GONE
                binding.date.visibility = View.GONE
                binding.button.visibility = View.GONE
                binding.tvInstructions.visibility = View.INVISIBLE
                binding.voiceButton.visibility = View.VISIBLE
                binding.nextBtn.visibility = View.GONE
                CountDown = viewModel.startTimer(binding, 60 * 1000)
            }
            else {
                binding.date.visibility = View.GONE
                binding.Text.visibility = View.VISIBLE
                binding.button.visibility = View.GONE
                binding.voiceButton.visibility = View.GONE
                CountDown = viewModel.startTimer(binding, 60 * 1000)
            }
            binding.tvQuestionNo.text = " Question " + newQuestion.question_no.toString() +
                    newQuestion.sub_question.toString()
            binding.tvInstructions.text = newQuestion.instruction
            binding.tvSubText.text = newQuestion.sub_text
            answer = newQuestion.answers.toString()
            tts!!.speak(newQuestion.instruction, TextToSpeech.QUEUE_FLUSH, null)
        })

        binding.button.setOnClickListener{
            binding.nextBtn.visibility = View.VISIBLE
            binding.bar.visibility = View.VISIBLE
            binding.button.visibility = View.GONE
            binding.trueButton.visibility = View.VISIBLE
            binding.falseButton.visibility = View.VISIBLE
            CountDown = viewModel.startTimer(binding, 60 * 1000)
        }
        // Go to the next question
        binding.nextBtn.setOnClickListener {
            if (binding.Text.visibility == View.VISIBLE){
                var string = binding.Text.text.toString()
                if (answer?.contains(string) == true){
                    Timber.i("Well Done")
                    // If string == answer do something
                }
            }
            CountDown?.cancel()
            viewModel.onNext()
        }
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
        binding.voiceButton.setOnClickListener{
            speak()
            binding.nextBtn.visibility = View.VISIBLE
        }

        // Go to the previous question
        //binding.btnPrevious.setOnClickListener {
        //    viewModel.onPrev()
        //}

        return binding.root
    }
    fun LoadImageFromWebOperations(url: String?): Drawable? {
        return try {
            val `is`: InputStream = URL(url).content as InputStream
            Drawable.createFromStream(`is`, "src name")
        } catch (e: java.lang.Exception) {
            null
        }
    }
}
