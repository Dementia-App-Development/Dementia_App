package com.dementiaquiz.android.fragments

import android.os.Bundle
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

/**
 * Displays the current quiz question and handles user input in answering the question
 */
class QuizFragment : Fragment() {

    private lateinit var viewModel: QuizViewModel

    private lateinit var binding: FragmentQuizBinding

    /**
     * Create views for the quiz by inflating XML
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

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

        // Set up LiveData observation relationships
        viewModel.currentQuestion.observe(viewLifecycleOwner, Observer<QuizQuestion> { newQuestion ->
            binding.tvTimeLimit.text = newQuestion.time_limit.toString()
            binding.tvQuestionNo.text = "id " + newQuestion.id.toString() + " | " + "Question " +
                    newQuestion.question_no.toString() + newQuestion.sub_question.toString()
            binding.tvInstructions.text = newQuestion.instruction.toString()
            binding.tvSubText.text = newQuestion.sub_text.toString()
        })

        // Go to the next question
        binding.btnNext.setOnClickListener {
            viewModel.onNext()
        }

        // Go to the previous question
        binding.btnPrevious.setOnClickListener {
            viewModel.onPrev()
        }

        return binding.root
    }
}

///**
// * Parses a json string and outputs a list of QuizQuestion objects
// */
//fun generateQuizQuestionsFromJson(jsonString: String): List<QuizQuestion> {
//    try {
//        val gson = Gson()
//        val quizQuestion = object : TypeToken<List<QuizQuestion>>() {}.type
//        return gson.fromJson(jsonString, quizQuestion)
//    } catch (e: Exception) {
//        Log.i(
//            "error:",
//            "could not generate quiz questions list from json string"
//        )
//        throw Exception("could not generate quiz questions list from json string")
//    }
//}