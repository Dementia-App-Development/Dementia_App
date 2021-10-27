package com.dementiaquiz.android.fragments

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