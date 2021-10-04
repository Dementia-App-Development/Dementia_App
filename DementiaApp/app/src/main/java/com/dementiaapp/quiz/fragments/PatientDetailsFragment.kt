package com.dementiaapp.quiz.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.dementiaapp.quiz.R
import com.dementiaapp.quiz.databinding.FragmentPatientDetailsBinding
import java.net.URL

/**
 * Displays a Pre-Quiz form for the assisted mode, whereby key details of the patient are inputted
 */
class PatientDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPatientDetailsBinding>(inflater,
            R.layout.fragment_patient_details, container, false)

        // Go to quiz button
        binding.btnGoToQuiz.setOnClickListener { v:View ->
            v.findNavController().navigate(com.dementiaapp.quiz.fragments.PatientDetailsFragmentDirections.actionPatientDetailsFragmentToQuizFragment())
        }

        return binding.root
    }
}

@SuppressLint("RestrictedApi")
fun getQuizFromURL() {
//    val latitude, longitude = getGPSCoordinates()
    val apiResponse = URL("https://alz-backend.herokuapp.com/question/all/").readText()
    Log.i("API RESPONSE", apiResponse)
//    Toast.makeText(getActivity().applicationContext, apiResponse, Toast.LENGTH_LONG).show()
}

fun getGPSCoordinates() {
//    val latitude: Double
//    val longitude: Double
//    return latitude, longitude
}