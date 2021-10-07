package com.dementiaapp.quiz

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dementiaapp.quiz.databinding.FragmentQuizBinding

import android.content.Intent
import android.os.Handler
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import java.util.*






/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment(),TextToSpeech.OnInitListener {
    val Min = 60 * 1000
    // Initialize the current question index to 0 (the first question)
    private var currentQuestionIndex = 0
    private val REQUEST_CODE_SPEECH_INPUT = 100;
    private var tts: TextToSpeech? = null
    val handler = Handler()
    override fun onInit(status: Int) {
        if(status != TextToSpeech.ERROR) {
            tts?.language = Locale.UK;
            tts!!.speak("What year is this?", TextToSpeech.QUEUE_FLUSH, null)
        }
    }
    public override fun onDestroy() {
        // Shutdown TTS
        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }
        super.onDestroy()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        tts = TextToSpeech(activity, this)
        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentQuizBinding>(
            inflater,
            R.layout.fragment_quiz,
            container,
            false
        )

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
            listOf("2021")
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
            listOf("2021")
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
            listOf("Spring")
        )
        var quizQuestions = listOf(questionOne, questionTwo)
        binding.bar.progress = 0
        startTimer(binding, Min)
        // Sort the questions list by id
        quizQuestions = quizQuestions.sortedBy { it.id }
        // Initialize the question fields in the UI
        populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)

        // When next button is pressed, go to next question so long as not at end of question list

        //handler.postDelayed({
        //    startTimer(binding, Min)
        //}, 100)
        // CountDown needs to go onto a handler!!!!!!
        var CountDown = startTimer(binding, Min)
        binding.btnNext.setOnClickListener {
            if (currentQuestionIndex < quizQuestions.size - 1) {
                CountDown.cancel()
                currentQuestionIndex += 1
                populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
                if (quizQuestions[currentQuestionIndex].response_type != QuizQuestion.ResponseType.ASSISTED){
                    CountDown = startTimer(binding, Min)
                }
                tts!!.speak(quizQuestions[currentQuestionIndex].instruction, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
        binding.button.setOnClickListener{
            binding.btnNext.visibility = View.VISIBLE
            binding.bar.visibility = View.VISIBLE
            binding.button.visibility = View.GONE
            binding.Text.visibility = View.VISIBLE
            CountDown = startTimer(binding, Min)
        }
        binding.voiceButton.setOnClickListener{
            speak();
            binding.btnNext.visibility = View.VISIBLE
        }
        // When prev button is pressed, go to prev question so long as not at start of question list
        //binding.prevBtn.setOnClickListener {
        //    if (currentQuestionIndex > 0) {
        //        currentQuestionIndex -= 1
        //        populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
        //    }
        // }


        return binding.root
    }
    private fun speak() {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            REQUEST_CODE_SPEECH_INPUT -> {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    // Get Text from result
                    val result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                    Log.i("Answer", result?.get(0).toString())

                    // Return result of question
                    //if (textViewVoiceAnswer.text == "no ifs ands or buts") {
                    //    textViewAnswerResult.text = "Correct"
                    //} else {
                    //    textViewAnswerResult.text = "Incorrect"
                    //}
                }
            }
        }
    }
}

private fun startTimer(binding: FragmentQuizBinding, Min: Int): CountDownTimer {
    Log.i("Timer", "Is this working")
    binding.bar.progress = 0
    val MyCountDownTimer = object : CountDownTimer(Min.toLong(), 1000) {
        override fun onTick(millisUntilFinished: Long) {

            val fraction = millisUntilFinished / Min.toDouble()
            binding.bar.progress = (fraction * 100).toInt()
        }

        override fun onFinish() {
            binding.bar.progress = 100
            this.cancel()
        }
    }
    MyCountDownTimer.start()
    return MyCountDownTimer
}
/**
 * This function populates the UI with the question at the provided index in the list of questions
 */
// TODO: temporary code for demonstration purposes only - needs tidy up when UI is done to display components properly
fun populateUIWithQuestion(
    binding: FragmentQuizBinding,
    quizQuestions: List<QuizQuestion>,
    i: Int
) {
    if (quizQuestions[i].response_type == QuizQuestion.ResponseType.DATE) {
        binding.Text.visibility = View.GONE
        binding.date.visibility = View.VISIBLE
        binding.button.visibility = View.GONE
        binding.voiceButton.visibility = View.GONE
    }
    else if (quizQuestions[i].response_type == QuizQuestion.ResponseType.ASSISTED) {
        binding.Text.visibility = View.GONE
        binding.date.visibility = View.GONE
        binding.button.visibility = View.VISIBLE
        binding.bar.visibility = View.GONE
        binding.btnNext.visibility = View.GONE
        binding.voiceButton.visibility = View.GONE
    }
    else if (quizQuestions[i].response_type == QuizQuestion.ResponseType.SPEECH) {
        binding.Text.visibility = View.GONE
        binding.date.visibility = View.GONE
        binding.button.visibility = View.GONE
        binding.voiceButton.visibility = View.VISIBLE
        binding.btnNext.visibility = View.GONE
    }
    else {
        binding.date.visibility = View.GONE
        binding.Text.visibility = View.VISIBLE
        binding.button.visibility = View.GONE
        binding.voiceButton.visibility = View.GONE
    }
    // Instantiate the fields in the main activity with the first question in the quiz
    binding.tvQuestionNo.text = " Question " + quizQuestions[i].question_no.toString() +
            quizQuestions[i].sub_question.toString()
    binding.tvInstructions.text = quizQuestions[i].instruction
    binding.tvSubText.text = quizQuestions[i].sub_text
}