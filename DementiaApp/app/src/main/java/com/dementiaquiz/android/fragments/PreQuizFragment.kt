package com.dementiaquiz.android.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentPreQuizBinding
import com.dementiaquiz.android.models.QuizViewModel
import timber.log.Timber

/**
 * Two-button display to determine whether the quiz is being conducted in Assisted or Patient mode
 */
class PreQuizFragment : Fragment() {

    private lateinit var viewModel: QuizViewModel

    // The default quiz parameters when the fragment is first opened
    private var isSolo : Boolean = true
    private var isAssisted : Boolean = false
    private var isAtHome : Boolean = true
    private var isAtClinic : Boolean = false

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPreQuizBinding>(inflater,
            R.layout.fragment_pre_quiz, container, false)

        // Get the viewmodel and set the quiz mode to solo
        viewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

        // By default, the quiz is set to solo mode
        viewModel.setQuizMode("solo")

        // Get user ID argument using by navArgs property delegate
        val preQuizFragmentArgs by navArgs<PreQuizFragmentArgs>()
        val userID = preQuizFragmentArgs.userID

        // Toggle the respective toggle buttons on/off as the other member of the pair is pressed
        binding.preQuizByMyselfButton.setOnClickListener {
            if (binding.preQuizByMyselfButton.isChecked) {
                binding.preQuizBeingAssistedButton.toggle()
            } else {
                binding.preQuizByMyselfButton.toggle()
            }

            // Set the new quiz mode, and poll for coordinates
            isSolo = true
            isAssisted = false
            viewModel.setQuizMode(getQuizModeString(isSolo, isAssisted, isAtHome, isAtClinic))
        }

        binding.preQuizBeingAssistedButton.setOnClickListener {
            if (binding.preQuizBeingAssistedButton.isChecked) {
                binding.preQuizByMyselfButton.toggle()
            } else {
                binding.preQuizBeingAssistedButton.toggle()
            }

            // Set the new quiz mode, and poll for coordinates
            isSolo = false
            isAssisted = true
            viewModel.setQuizMode(getQuizModeString(isSolo, isAssisted, isAtHome, isAtClinic))
        }

        binding.preQuizAtHomeButton.setOnClickListener {
            if (binding.preQuizAtHomeButton.isChecked) {
                binding.preQuizAtClinicButton.toggle()
            } else {
                binding.preQuizAtHomeButton.toggle()
            }

            // Set the new quiz mode, and poll for coordinates
            isAtHome = true
            isAtClinic = false
            viewModel.setQuizMode(getQuizModeString(isSolo, isAssisted, isAtHome, isAtClinic))
        }

        binding.preQuizAtClinicButton.setOnClickListener {
            if (binding.preQuizAtClinicButton.isChecked) {
                binding.preQuizAtHomeButton.toggle()
            } else {
                binding.preQuizAtClinicButton.toggle()
            }

            // Set the new quiz mode, and poll for coordinates
            isAtHome = false
            isAtClinic = true
            viewModel.setQuizMode(getQuizModeString(isSolo, isAssisted, isAtHome, isAtClinic))
        }

        // Detect when quiz has finished loading, once it has, show the go to quiz button
        viewModel.quizIsLoading.observe(viewLifecycleOwner, Observer<Boolean> {updatedQuizIsLoading ->
            if (updatedQuizIsLoading) {
                binding.preQuizGoToQuizButton.visibility = View.GONE
            } else {
                binding.preQuizGoToQuizButton.visibility = View.VISIBLE
            }
        })

        // Navigate to the quiz passing user ID as argument
        binding.preQuizGoToQuizButton.setOnClickListener { v:View ->
            // Pass the user ID argument to the quiz fragment
            val action = PreQuizFragmentDirections.actionPreQuizFragmentToQuizFragment(userID)
            v.findNavController().navigate(action)
        }

        return binding.root
    }
}

// Gets the quiz mode string based on the boolean variables set by the UI
private fun getQuizModeString(
    isSolo: Boolean,
    isAssisted: Boolean,
    isAtHome: Boolean,
    isAtClinic: Boolean
): String {
    if (isSolo) {
        Timber.i("Quiz mode set to SOLO")
        return "solo"
    }
    if (isAssisted && isAtHome) {
        Timber.i("Quiz mode set to ASSISTED-HOME")
        return "assisted-home"
    }
    if (isAssisted && isAtClinic) {
        Timber.i("Quiz mode set to ASSISTED-FACILITY")
        return "assisted-facility"
    }
    // Default fallback scenario is to set quiz to solo mode
    return "solo"
}