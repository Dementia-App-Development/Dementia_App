package com.dementiaquiz.android.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentPatientDetailsBinding
import timber.log.Timber
import java.net.URL

/**
 * Displays a Pre-Quiz form for the assisted mode, whereby key details of the patient are inputted
 */
class PatientDetailsFragment : Fragment() {

//    // Available genders for the gender spinner
    //TODO delete this
//    private val genders : List<String> = listOf("Male", "Female", "Other")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        Timber.i("onCreateView called")

        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPatientDetailsBinding>(inflater,R.layout.fragment_patient_details, container, false)

        // Go to quiz button
        binding.patientDetailsGoToQuizButton.setOnClickListener { v:View ->
            v.findNavController().navigate(R.id.action_patientDetailsFragment_to_quizFragment)
        }

        // Toggle the respective toggle buttons on/off as the other member of the pair is pressed
        binding.patientDetailsAtHomeButton.setOnClickListener {
            if (binding.patientDetailsAtHomeButton.isChecked) {
                binding.patientDetailsAtClinicButton.toggle()
            } else {
                binding.patientDetailsAtHomeButton.toggle()
            }
        }
        binding.patientDetailsAtClinicButton.setOnClickListener {
            if (binding.patientDetailsAtClinicButton.isChecked) {
                binding.patientDetailsAtHomeButton.toggle()
            } else {
                binding.patientDetailsAtClinicButton.toggle()
            }
        }
        binding.patientDetailsByMyselfButton.setOnClickListener {
            if (binding.patientDetailsByMyselfButton.isChecked) {
                binding.patientDetailsBeingAssistedButton.toggle()
            } else {
                binding.patientDetailsByMyselfButton.toggle()
            }
        }
        binding.patientDetailsBeingAssistedButton.setOnClickListener {
            if (binding.patientDetailsBeingAssistedButton.isChecked) {
                binding.patientDetailsByMyselfButton.toggle()
            } else {
                binding.patientDetailsBeingAssistedButton.toggle()
            }
        }

        return binding.root
    }
}

@SuppressLint("RestrictedApi")
fun getQuizFromURL() {
//    val latitude, longitude = getGPSCoordinates()
    val apiResponse = URL("https://alz-backend.herokuapp.com/question/all/").readText()
    Timber.i("API RESPONSE")
//    Toast.makeText(getActivity().applicationContext, apiResponse, Toast.LENGTH_LONG).show()
}

fun getGPSCoordinates() {
//    val latitude: Double
//    val longitude: Double
//    return latitude, longitude
}