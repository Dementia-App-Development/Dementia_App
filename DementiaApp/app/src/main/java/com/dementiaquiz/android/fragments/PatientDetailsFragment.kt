package com.dementiaquiz.android.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.database.model.User
import com.dementiaquiz.android.databinding.FragmentPatientDetailsBinding
import com.dementiaquiz.android.models.QuizViewModel
import com.dementiaquiz.android.models.UsersViewModel
import com.dementiaquiz.android.models.UsersViewModelFactory
import timber.log.Timber
import java.net.URL
import java.util.*
import android.widget.EditText
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi


/**
 * Displays a Pre-Quiz form for the assisted mode, whereby key details of the patient are inputted
 */
class PatientDetailsFragment : Fragment() {

    private val usersViewModel: UsersViewModel by viewModels {
        UsersViewModelFactory((activity?.application as DementiaQuizApplication).userRepository)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPatientDetailsBinding>(inflater,
            com.dementiaquiz.android.R.layout.fragment_patient_details, container, false)

        // Go to quiz button
        binding.patientDetailsNextButton.setOnClickListener { v:View ->

            // TODO Write to the users database with the info provided by the user in the UI
            val user = createNewUser(binding)
            usersViewModel.checkAndInsert(user)
            Timber.i("Created new user in db")

            // TODO: only navigate if the fields are all populated, else show a warning / pop-up and change colour of empty edit text fields to red
            val userID = 0 // TODO: get the user ID here so it can be passed to pre quiz
            val action = PatientDetailsFragmentDirections.actionPatientDetailsFragmentToPreQuizFragment(userID)
            v.findNavController().navigate(action)
        }

        // Open date picker dialog when date of birth edit text is opened
        binding.patientDetailsDOBEditText.setOnClickListener { v:View ->
            context?.let { showDatePickerDialog(it, binding) }
        }

        return binding.root
    }
}

/**
 * Displays the date picker dialog and sets the text field equal to the users input
 */
@RequiresApi(Build.VERSION_CODES.N)
private fun showDatePickerDialog(context : Context, binding : FragmentPatientDetailsBinding) {
    val datePickerDialog : DatePickerDialog = DatePickerDialog(context)
    // Default year of birth is set to 1940
    datePickerDialog.updateDate(1940,1,1)
    datePickerDialog.datePicker.touchables[0].performClick();
    datePickerDialog.show()
    datePickerDialog.setOnDateSetListener { _, year, month, day ->
        val dobString = ("$day/$month/$year")
        binding.patientDetailsDOBEditText.setText(dobString)
    }
}

/**
 * Returns a user type object from the fields populated in the UI
 */
private fun createNewUser(binding: FragmentPatientDetailsBinding) : User{
    val nickname = binding.patientDetailsNicknameEditText.toString()
    val firstName = binding.patientDetailsFirstNameEditText.toString()
    val lastName = binding.patientDetailsLastNameEditText.toString()
    val dateOfBirth = binding.patientDetailsDOBEditText
    val gender = binding.patientDetailsGenderSpinner.toString()
    // TODO: place holder values Date() and gender need to be the correct type
    return User(0, nickname, firstName, lastName, Date(), true)
}