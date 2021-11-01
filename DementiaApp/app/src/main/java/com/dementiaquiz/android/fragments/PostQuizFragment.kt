package com.dementiaquiz.android.fragments

import android.graphics.Color
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
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
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
        binding.viewKonfetti.build()
            .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
            .setDirection(0.0, 359.0)
            .setSpeed(1f, 5f)
            .setFadeOutEnabled(true)
            .setTimeToLive(2000L)
            .addShapes(Shape.Square, Shape.Circle)
            .addSizes(Size(12))
            .setPosition(-50f, binding.viewKonfetti.width + 50f, -50f, -50f)
            .streamFor(300, 5000L)
        /* The main_menu button is removed to avoid too many main_menu fragments existing in the app stack at the same time
        // Navigate back to the title screen
        binding.mainMenuButton.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_postQuizFragment_to_titleFragment)
        }*/

        quizResultViewModel.getQuizResultByResultId(1).observe(viewLifecycleOwner) { result ->

            if (result != null) {
                binding.scoreDescriptionTextView.text = "Score"

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

                binding.commentContentTextView.text = comment

            }else{

                //There is not such result
                binding.scoreTextView.text = ""
                binding.scoreDescriptionTextView.text = "Not Available"
                binding.commentContentTextView.text = "Something is wrong, the score is not available"


            }

        }

        return binding.root
    }


    /*override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        application = activity?.application as DementiaQuizApplication
    }*/
}