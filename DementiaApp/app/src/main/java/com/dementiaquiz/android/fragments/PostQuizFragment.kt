package com.dementiaquiz.android.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentPostQuizBinding
import com.dementiaquiz.android.databinding.FragmentTitleBinding
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import timber.log.Timber

/**
 * A fragment displayed at the end of the quiz, to display the results of the quiz
 */
class PostQuizFragment : Fragment() {

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
        // Navigate back to the title screen
        binding.postQuizDoneButton.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_postQuizFragment_to_titleFragment)
        }

        // Get args using by navArgs property delegate and display the score from the quiz
        val postQuizFragmentArgs by navArgs<PostQuizFragmentArgs>()
        binding.postQuizScoreTextView.text = postQuizFragmentArgs.currentScore.toString()

        return binding.root
    }
}