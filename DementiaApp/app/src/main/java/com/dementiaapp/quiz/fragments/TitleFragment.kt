package com.dementiaapp.quiz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dementiaapp.quiz.R
import com.dementiaapp.quiz.databinding.FragmentTitleBinding

/**
 * Home screen menu when first opening the app
 */
class TitleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title, container, false)

        // Use Nav Controller to set quiz button to navigate to quiz fragment
        binding.btnQuiz.setOnClickListener { v: View ->
            v.findNavController().navigate(com.dementiaapp.quiz.fragments.TitleFragmentDirections.actionTitleFragmentToPreQuizFragment2())
        }

        // Use Nav Controller to set results button to navigate to results fragment
        binding.btnResults.setOnClickListener { v: View ->
            v.findNavController()
                .navigate(com.dementiaapp.quiz.fragments.TitleFragmentDirections.actionTitleFragmentToResultsFragment())
        }

        return binding.root
    }
}