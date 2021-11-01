package com.dementiaquiz.android.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentPreQuizBinding
import com.dementiaquiz.android.models.QuizViewModel
import timber.log.Timber

/**
 * Two-button display to determine whether the quiz is being conducted in Assisted or Patient mode
 */
class PreQuizFragment : Fragment() {

    private lateinit var viewModel: QuizViewModel

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.i("onCreateView called")

        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPreQuizBinding>(inflater,
            R.layout.fragment_pre_quiz, container, false)

        // Get the viewmodel and set the quiz mode to solo
        viewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)
        viewModel.setQuizMode("solo")

        // If in assisted mode, go to the patient details fragment
        binding.preQuizAssistingButton.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_preQuizFragment_to_patientDetailsFragment)
        }

        // If in patient mode, go straight to the quiz
        binding.preQuizForMyselfButton.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_preQuizFragment_to_quizFragment)
        }

        return binding.root
    }
}