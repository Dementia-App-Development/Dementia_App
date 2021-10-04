package com.dementiaapp.quiz.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dementiaapp.quiz.R
import com.dementiaapp.quiz.databinding.FragmentPreQuizBinding

/**
 * Two-button display to determine whether the quiz is being conducted in Assisted or Patient mode
 */
class PreQuizFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPreQuizBinding>(inflater,
            R.layout.fragment_pre_quiz, container, false)

        // If in assisted mode, go to the patient details fragment
        binding.btnAssisting.setOnClickListener { v: View ->
            v.findNavController().navigate(com.dementiaapp.quiz.fragments.PreQuizFragmentDirections.actionPreQuizFragmentToPatientDetailsFragment())
        }

        // If in patient mode, go straight to the quiz
        binding.btnForMyself.setOnClickListener { v: View ->
            v.findNavController().navigate(com.dementiaapp.quiz.fragments.PreQuizFragmentDirections.actionPreQuizFragmentToQuizFragment())
        }

        return binding.root
    }
}

// Parse the quiz questions json from file and create quiz questions kotlin object
//TODO: below code was in main activity, it fetched quiz questions from storage
// instead we will require internet connectivity to fetch from server
fun getQuizQuestions() {
//    val jsonFileString = getJsonDataFromAsset(applicationContext, QUIZ_QUESTIONS_JSON)
//    val quizQuestions = generateQuizQuestions(jsonFileString)
//    Log.i("quiz data", quizQuestions.toString())
}