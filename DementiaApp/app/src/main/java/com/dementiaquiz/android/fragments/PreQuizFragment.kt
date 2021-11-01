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
import androidx.navigation.findNavController
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentPreQuizBinding
import timber.log.Timber

/**
 * Two-button display to determine whether the quiz is being conducted in Assisted or Patient mode
 */
class PreQuizFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.i("onCreateView called")

        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPreQuizBinding>(inflater,
            R.layout.fragment_pre_quiz, container, false)

        // Prompt the user for location permission
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

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