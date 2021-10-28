package com.dementiaquiz.android.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentPostQuizBinding
import com.dementiaquiz.android.databinding.FragmentTitleBinding
import com.dementiaquiz.android.models.QuizResultViewModel
import com.dementiaquiz.android.models.QuizResultViewModelFactory
import com.dementiaquiz.android.models.QuizViewModel
import timber.log.Timber

/**
 * A fragment displayed at the end of the quiz, to display the results of the quiz
 */
class PostQuizFragment : Fragment() {


    //TODO: this may crash the app, there might be another better solution
    // see "https://stackoverflow.com/questions/11585702/how-to-get-application-object-into-fragment-class"
    private val quizResultViewModel: QuizResultViewModel by viewModels {
        QuizResultViewModelFactory((activity?.application as DementiaQuizApplication).quizResultRepository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPostQuizBinding>(
            inflater, R.layout.fragment_post_quiz, container, false
        )

        // Navigate back to the title screen
        binding.mainMenuButton.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_postQuizFragment_to_titleFragment)
        }

        quizResultViewModel.getQuizResultByResultId(2).observe(viewLifecycleOwner) { result ->

            if (result != null) {

                println("the score is: "+ result.score.toString())
                //Log.d("the score is: ", result.score.toString())
                binding.scoreTextView.text = result.score.toString()

            }else{

                binding.scoreTextView.text = ""
                binding.scoreDescriptionTextView.text = "We are unable to find your score, please contact the administrator"

            }

        }

        return binding.root
    }


    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        application = activity?.application as DementiaQuizApplication
    }*/
}