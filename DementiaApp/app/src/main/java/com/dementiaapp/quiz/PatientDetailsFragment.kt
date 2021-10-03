package com.dementiaapp.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dementiaapp.quiz.databinding.FragmentPatientDetailsBinding
import com.dementiaapp.quiz.databinding.FragmentPreQuizBinding

/**
 * A simple [Fragment] subclass.
 * Use the [PatientDetailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PatientDetailsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPatientDetailsBinding>(inflater, R.layout.fragment_patient_details, container, false)

        // Go to quiz button
        binding.btnGoToQuiz.setOnClickListener { v:View ->
            v.findNavController().navigate(PatientDetailsFragmentDirections.actionPatientDetailsFragmentToQuizFragment())
        }

        return binding.root
    }
}