package com.dementiaapp.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dementiaapp.quiz.databinding.FragmentPreQuizBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PreQuizFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PreQuizFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPreQuizBinding>(inflater, R.layout.fragment_pre_quiz, container, false)

        // Use Nav Controller to set quiz button to navigate to quiz fragment
        binding.goToQuizButton.setOnClickListener { v: View ->
            v.findNavController().navigate(PreQuizFragmentDirections.actionPreQuizFragment2ToQuizFragment())
        }

        return binding.root
    }
}