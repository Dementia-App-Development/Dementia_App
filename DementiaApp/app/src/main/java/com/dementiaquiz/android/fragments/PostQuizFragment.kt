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

        quizResultViewModel.getQuizResultByResultId(1).observe(viewLifecycleOwner) { result ->

            if (result != null) {

                println("the score is: "+ result.score.toString())
                //Log.d("the score is: ", result.score.toString())
                binding.scoreTextView.text = result.score.toString()

                var comment:String

                when(result.score){
                    in 80..100 -> comment="Congratulation! Based on the test result, you have no symptom of dementia"
                    in 50..80 -> comment="Warning! Based on the test result, you have mild symptom of dementia. We recommend you to see a Doctor if possible"
                    in 0..50 -> comment="Warning! Based on the test result, you have severe symptom of dementia. Please inform your family and see a Doctor as soon as possible. "
                    else -> comment="Something is wrong, we are not able to read the scaled test score. Please contact the Administrator"
                }

                binding.commentTextView.text = comment

            }else{

                binding.scoreTextView.text = ""
                binding.scoreDescriptionTextView.text = "We are unable to find your score, please contact the Administrator"

            }

        }

        return binding.root
    }


    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        application = activity?.application as DementiaQuizApplication
    }*/
}