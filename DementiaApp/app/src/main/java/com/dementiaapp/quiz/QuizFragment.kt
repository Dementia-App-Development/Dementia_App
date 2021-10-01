package com.dementiaapp.quiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.dementiaapp.quiz.databinding.ActivityMainBinding
import com.dementiaapp.quiz.databinding.FragmentQuizBinding

/**
 * A simple [Fragment] subclass.
 * Use the [QuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizFragment : Fragment() {

    // Initialize the current question index to 0 (the first question)
    private var currentQuestionIndex = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentQuizBinding>(inflater, R.layout.fragment_quiz, container, false)

        // Parse the quiz questions json from file and create quiz questions kotlin object
        //TODO: below code was in main activity, needs to be in "PreQuizFragment" (yet to be created) - will require internet connectivity
//        val jsonFileString = getJsonDataFromAsset(applicationContext, QUIZ_QUESTIONS_JSON)
//        var quizQuestions = generateQuizQuestions(jsonFileString)
//        Log.i("quiz data", quizQuestions.toString())
//
//        // Sort the questions list by id
//        quizQuestions = quizQuestions.sortedBy {it.id}
//
//        // Initialize the question fields in the UI
//        populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
//
//        // When next button is pressed, go to next question so long as not at end of question list
//        binding.nextBtn.setOnClickListener {
//            if (currentQuestionIndex < quizQuestions.size - 1) {
//                currentQuestionIndex += 1
//                populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
//            }
//        }
//
//        // When prev button is pressed, go to prev question so long as not at start of question list
//        binding.prevBtn.setOnClickListener {
//            if (currentQuestionIndex > 0) {
//                currentQuestionIndex -= 1
//                populateUIWithQuestion(binding, quizQuestions, currentQuestionIndex)
//            }
//        }

        return binding.root
    }
}

/**
 * This function populates the UI with the question at the provided index in the list of questions
 */
// TODO: temporary code for demonstration purposes only
fun populateUIWithQuestion(binding: FragmentQuizBinding, quizQuestions: List<QuizQuestion>, i: Int) {
    // Instantiate the fields in the main activity with the first question in the quiz
    binding.tvTimeLimit.text = quizQuestions[i].time_limit.toString() + " seconds to answer"
    binding.tvQuestionNo.text = "id: " + quizQuestions[i].id.toString() + " | " +
            "Question " + quizQuestions[i].question_no.toString() +
            quizQuestions[i].sub_question.toString()
    binding.tvInstructions.text = quizQuestions[i].instruction
    binding.tvSubText.text = quizQuestions[i].sub_text
}