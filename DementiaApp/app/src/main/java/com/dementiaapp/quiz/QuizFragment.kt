package com.dementiaapp.quiz

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dementiaapp.quiz.databinding.FragmentQuizBinding

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment() {
    val Min = 60 * 1000
    // Initialize the current question index to 0 (the first question)
    private var currentQuestionIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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
        val questionTwo = QuizQuestion(
            2,
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
        var CountDown = startTimer(binding, Min)
        binding.btnNext.setOnClickListener {
            if (currentQuestionIndex < quizQuestions.size - 1) {
                CountDown.cancel()
                currentQuestionIndex += 1
                populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
                startTimer(binding, Min)
            }
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
            binding.bar.progress = 0
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
    }
    else {
        binding.date.visibility = View.GONE
        binding.Text.visibility = View.VISIBLE
    }
    // Instantiate the fields in the main activity with the first question in the quiz
    binding.tvQuestionNo.text = " Question " + quizQuestions[i].question_no.toString() +
            quizQuestions[i].sub_question.toString()
    binding.tvInstructions.text = quizQuestions[i].instruction
    binding.tvSubText.text = quizQuestions[i].sub_text
}