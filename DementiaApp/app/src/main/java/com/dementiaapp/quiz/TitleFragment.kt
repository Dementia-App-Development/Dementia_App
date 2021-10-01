package com.dementiaapp.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dementiaapp.quiz.databinding.FragmentQuizBinding
import com.dementiaapp.quiz.databinding.FragmentTitleBinding

/**
 * A simple [Fragment] subclass.
 * Use the [TitleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TitleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater, R.layout.fragment_title, container, false)

        // Use Nav Controller to set quiz button to navigate to quiz fragment
        binding.quizButton.setOnClickListener { v: View ->
            v.findNavController().navigate(TitleFragmentDirections.actionTitleFragmentToPreQuizFragment2())
        }

        // Use Nav Controller to set results button to navigate to results fragment
        binding.resultsButton.setOnClickListener { v: View ->
            v.findNavController()
                .navigate(TitleFragmentDirections.actionTitleFragmentToResultsFragment())
        }

        return binding.root
    }
}