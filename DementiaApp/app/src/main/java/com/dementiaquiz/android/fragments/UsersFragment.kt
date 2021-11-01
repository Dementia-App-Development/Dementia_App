package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentUsersBinding

/**
 * TODO: description
 */
class UsersFragment : Fragment() {

    private lateinit var binding : FragmentUsersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        // Use view binding to get variables from XML
        binding = DataBindingUtil.inflate<FragmentUsersBinding>(inflater,R.layout.fragment_users, container, false)

        // Toggle the existing users and new user buttons to mirror each other
        binding.usersExistingUserToggleButton.setOnClickListener {
            if (binding.usersExistingUserToggleButton.isChecked) {
                binding.usersNewUserToggleButton.toggle()
            } else {
                binding.usersExistingUserToggleButton.toggle()
            }

            // Display the existing users spinner if existing users button is on
            binding.usersExistingUsersSpinner.visibility = View.VISIBLE
        }
        binding.usersNewUserToggleButton.setOnClickListener {
            if (binding.usersNewUserToggleButton.isChecked) {
                binding.usersExistingUserToggleButton.toggle()
            } else {
                binding.usersNewUserToggleButton.toggle()
            }

            // Hide the existing users spinner if new users button is on
            binding.usersExistingUsersSpinner.visibility = View.GONE
        }

        // Go to quiz dependent on whether new or existing user
        binding.usersNextButton.setOnClickListener { v:View ->
            // If it is a new user, navigate to the patient details fragment
            if (binding.usersNewUserToggleButton.isChecked) {
                v.findNavController().navigate(R.id.action_usersFragment_to_patientDetailsFragment)

            // If it is an existing user, navigate straight to the pre-quiz fragment
            } else {
                v.findNavController().navigate(R.id.action_usersFragment_to_preQuizFragment)
            }
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        // Display the existing users spinner if existing users button is on (simple check if navigating back to this fragment)
        if (binding.usersExistingUserToggleButton.isChecked) {
            binding.usersExistingUsersSpinner.visibility = View.VISIBLE
        }
    }
}