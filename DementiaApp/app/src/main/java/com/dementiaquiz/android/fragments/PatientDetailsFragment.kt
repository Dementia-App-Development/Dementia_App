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
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.dementiaquiz.android.utils.TimeConverter
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


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

            val user = createNewUser(binding)
            Timber.i("the user is: $user")

            // TODO: only saveToDatabase and navigate if the fields are all populated, else show a warning / pop-up and change colour of empty edit text fields to red

            // TODO:(Done) Write to the users database with the info provided by the user in the UI
            usersViewModel.checkAndInsert(user).observe(viewLifecycleOwner){ userId ->
                if (userId==(-1L)){
                    // failed, indicate there is already a user with the same nickname in hte database
                    Toast.makeText(context,
                        "Fail, your nickname has been used by others, please try again",
                        Toast.LENGTH_SHORT).show()
                }else{
                    // success inserted, navigate to next fragment
                    Toast.makeText(context,
                        "Success, your new account is: ${user.nickname}",
                        Toast.LENGTH_SHORT).show()
                    Timber.i("Created new user in db")

                    // TODO:(Done) get the user ID here so it can be passed to pre quiz
                    val action = PatientDetailsFragmentDirections.actionPatientDetailsFragmentToPreQuizFragment(userId)

                    v.findNavController().navigate(action)
                }
            }

            Timber.i("Created new user in db")

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
        val dobString = ("$year-${month+1}-$day")
        binding.patientDetailsDOBEditText.setText(dobString)
    }
}

/**
 * Returns a user type object from the fields populated in the UI
 */
private fun createNewUser(binding: FragmentPatientDetailsBinding) : User{
    val nickname = binding.patientDetailsNicknameEditText.text.toString()
    val firstName = binding.patientDetailsFirstNameEditText.text.toString()
    val lastName = binding.patientDetailsLastNameEditText.text.toString()
    val dateOfBirth = binding.patientDetailsDOBEditText.text.toString()
    val gender = binding.patientDetailsGenderSpinner.selectedItem.toString()

    // parse dateOfBirth String to LocalDate
    val dateArray = dateOfBirth.split("-").toTypedArray()
    var parsedDate = LocalDate.of(dateArray[0].toInt(), dateArray[1].toInt(), dateArray[2].toInt())

    // TODO: place holder values Date() and gender need to be the correct type
    return User(0, nickname, firstName, lastName, TimeConverter.convertToDateViaInstant(parsedDate), gender)
}

