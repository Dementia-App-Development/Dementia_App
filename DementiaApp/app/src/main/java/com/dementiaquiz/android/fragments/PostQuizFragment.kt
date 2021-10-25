package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
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
    ): View? {

        Timber.i("onCreateView called")

        // Inflate view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPostQuizBinding>(
            inflater, R.layout.fragment_post_quiz, container, false
        )

        // Navigate back to the title screen
        binding.btnDone.setOnClickListener { v: View ->
            Timber.i("Done button pressed")
            v.findNavController().navigate(R.id.action_postQuizFragment_to_titleFragment)
        }

        return binding.root
    }
}