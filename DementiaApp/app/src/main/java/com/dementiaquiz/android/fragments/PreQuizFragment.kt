package com.dementiaquiz.android.fragments

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import java.security.Permission

/**
 * Two-button display to determine whether the quiz is being conducted in Assisted or Patient mode
 */
class PreQuizFragment : Fragment() {

    private lateinit var quizViewModel: QuizViewModel

    // The default quiz parameters when the fragment is first opened
    private var isSolo : Boolean = true
    private var isAssisted : Boolean = false
    private var isAtHome : Boolean = true
    private var isAtClinic : Boolean = false

    private val locationPermissionRequest = registerForActivityResult(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ask for GPS permission if it is not granted
        if (context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_COARSE_LOCATION) } != PackageManager.PERMISSION_GRANTED ||
            context?.let { ActivityCompat.checkSelfPermission(it, Manifest.permission.ACCESS_FINE_LOCATION) } != PackageManager.PERMISSION_GRANTED){
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPreQuizBinding>(inflater,
            R.layout.fragment_pre_quiz, container, false)

        // Get the viewmodel and set the quiz mode to solo
        quizViewModel = ViewModelProvider(requireActivity()).get(QuizViewModel::class.java)

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
        }

        // Detect when quiz has finished loading, once it has, show the go to quiz button
        quizViewModel.quizIsLoading.observe(viewLifecycleOwner, Observer<Boolean> {quizIsLoading ->

            // If quiz is not loading and the go to quiz button is hidden (after it is clicked) then navigate to quiz
            if (!quizIsLoading && binding.preQuizGoToQuizButton.visibility == View.GONE) {
                val action = PreQuizFragmentDirections.actionPreQuizFragmentToQuizFragment(userID)
                view?.findNavController()?.navigate(action)
            }
        })

        // Navigate to the quiz passing user ID as argument
        binding.preQuizGoToQuizButton.setOnClickListener { v:View ->

            // Upon pressing go to quiz, load the quiz based on the parameters in the UI
            quizViewModel.setQuizMode(getQuizModeString(isSolo, isAssisted, isAtHome, isAtClinic))

            binding.preQuizGoToQuizButton.visibility = View.GONE
            binding.preQuizProgressBar.visibility = View.VISIBLE
        }

        // Observe whether quiz has fetched GPS coordinates, if it cannot then show dialog and navigate to title
        quizViewModel.gpsCoordinatesFetched.observe(viewLifecycleOwner, Observer<Boolean> { gpsCoordinatesFetched ->
            if (!gpsCoordinatesFetched && binding.preQuizGoToQuizButton.visibility == View.GONE) {
                context?.let { view?.let { it1 -> showCannotFetchGPSDialog(it, it1, quizViewModel) } }
            }
        })

        return binding.root
    }
}


/**
 * Shows a dialog that informs the user that GPS coordinates cannot be fetched, navigates to the title screen
 */
private fun showCannotFetchGPSDialog(context : Context,
                                     view : View,
                                     quizViewModel : QuizViewModel) {

    val builder = AlertDialog.Builder(context)
    builder.setTitle("Cannot fetch GPS coordinates")
    builder.setMessage("Please check your location settings")
    builder.setIcon(android.R.drawable.ic_dialog_alert)
    builder.setPositiveButton("Go Back to Main Menu"){dialogInterface, which ->
        view.findNavController().navigate(R.id.action_preQuizFragment_to_titleFragment)
    }
    val alertDialog: AlertDialog = builder.create()
    alertDialog.setCancelable(false)
    alertDialog.show()
}

/**
 * Gets the quiz mode string based on the boolean variables set by the UI
 */
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