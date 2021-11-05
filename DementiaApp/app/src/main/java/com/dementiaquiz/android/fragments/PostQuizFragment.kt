package com.dementiaquiz.android.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentPostQuizBinding
import com.dementiaquiz.android.models.QuizResultViewModel
import com.dementiaquiz.android.models.QuizResultViewModelFactory
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size

/**
 * A fragment displayed at the end of the quiz, to display the results of the quiz
 */
class PostQuizFragment : Fragment() {

    private val quizResultViewModel: QuizResultViewModel by viewModels {
        QuizResultViewModelFactory((activity?.application as DementiaQuizApplication).quizResultRepository)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Inflate view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPostQuizBinding>(
            inflater, R.layout.fragment_post_quiz, container, false
        )

        // The main_menu button is removed to avoid too many main_menu fragments existing in the app stack at the same time
        // Navigate back to the title screen
        binding.goToMainMenuButton.setOnClickListener {
            activity?.onBackPressed()
        }

        // Get the userId, resultID and score from the QuizFragment
        val postQuizFragmentArgs by navArgs<PostQuizFragmentArgs>()
        val resultID = postQuizFragmentArgs.resultID

        // Observe if there is a new result of the quiz
        quizResultViewModel.getQuizResultByResultId(resultID).observe(viewLifecycleOwner) { result ->

            if (result != null) {
                binding.postQuizScoreDescTextView.text = "Score"

                println("the score is: "+ result.score.toString())
                binding.postQuizScoreNumberTextView.text = result.score.toString()+"%"

                if (result.score >= 80){
                    binding.postQuizViewKonfetti.build()
                        .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                        .setDirection(0.0, 359.0)
                        .setSpeed(1f, 5f)
                        .setFadeOutEnabled(true)
                        .setTimeToLive(2000L)
                        .addShapes(Shape.Square, Shape.Circle)
                        .addSizes(Size(12))
                        .setPosition(-50f, binding.postQuizViewKonfetti.width + 50f, -50f, -50f)
                        .streamFor(300, 5000L)
                }
                val comment:String = when(result.score){
                    in 80..100 -> "Based on today's result, your cognitive function is normal."
                    in 50..80 -> "Based on today's result, you have mild symptoms of cognitive impairment. We recommend a consultation with your GP."
                    in 0..50 -> "Based on today's result, you have some symptoms of cognitive impairment. We recommend a consultation with your GP and further testing."
                    else -> "Something is wrong, we are not able to read the scaled test score. Please contact the Administrator."
                }

                binding.postQuizCommentContentTextView.text = comment

            } else {

                // There is not such result
                binding.postQuizScoreNumberTextView.text = ""
                binding.postQuizScoreDescTextView.text = "Not Available"
                binding.postQuizCommentContentTextView.text = "Something is wrong, the score is not available"

            }

        }

        return binding.root
    }

}