package com.dementiaquiz.android.fragments

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

/**
 * Displays a Pre-Quiz form for the assisted mode, whereby key details of the patient are inputted
 */
class PatientDetailsFragment : Fragment() {

    private val usersViewModel: UsersViewModel by viewModels {
        UsersViewModelFactory((activity?.application as DementiaQuizApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        Timber.i("onCreateView called")

        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentPatientDetailsBinding>(
            inflater,
            R.layout.fragment_patient_details,
            container,
            false
        )


        // Go to quiz button
        binding.patientDetailsNextButton.setOnClickListener { v: View ->

            // TODO Write to the users database with the info provided by the user in the UI
            val user = createNewUser(binding)
            usersViewModel.checkAndInsert(user).observe(viewLifecycleOwner){ result->
                println("usersViewModel.checkAndInsert(user).observe, result it: $result")

                if(result==(-1L)){
                    // failed, indicate there is already a user with the same nickname in hte database
                    Toast.makeText(context,
                        "Fail, your nickname has been used by others, please try again",
                        Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,
                        "Success, your new account is: ${user.nickname}",
                        Toast.LENGTH_SHORT).show()
                    Timber.i("Created new user in db")
                    v.findNavController().navigate(R.id.action_patientDetailsFragment_to_preQuizFragment)
                }
            }

        }

        return binding.root
    }
}

private fun createNewUser(binding: FragmentPatientDetailsBinding): User {
    val nickname = binding.patientDetailsNicknameEditText.text.toString()
    val firstName = binding.patientDetailsFirstNameEditText.text.toString()
    val lastName = binding.patientDetailsLastNameEditText.text.toString()
    val dateOfBirth = binding.patientDetailsAgeEditText.text.toString()
    val gender = binding.patientDetailsGenderSpinner.selectedItem.toString()
    //TODO: place holder values Date() and gender need to be correct type
    return User(0, nickname, firstName, lastName, Date(), true)
}