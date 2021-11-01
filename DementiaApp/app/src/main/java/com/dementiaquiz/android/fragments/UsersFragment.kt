package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentUsersBinding
import com.dementiaquiz.android.models.QuizResultViewModel
import com.dementiaquiz.android.models.QuizResultViewModelFactory
import com.dementiaquiz.android.models.UsersViewModel
import com.dementiaquiz.android.models.UsersViewModelFactory

/**
 * TODO: description
 */
class UsersFragment : Fragment() {

    //TODO: this may crash the app, there might be another better solution
    // see "https://stackoverflow.com/questions/11585702/how-to-get-application-object-into-fragment-class"
    private val usersViewModel: UsersViewModel by viewModels {
        UsersViewModelFactory((activity?.application as DementiaQuizApplication).userRepository)
    }

    private lateinit var binding : FragmentUsersBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Use view binding to get variables from XML
        binding = DataBindingUtil.inflate<FragmentUsersBinding>(inflater,R.layout.fragment_users, container, false)

        // TODO: (Done)Populate existing users spinner with the nicknames from the user database
        usersViewModel.getAllNicknames().observe(viewLifecycleOwner){ nicknameList->

            val spinner = binding.usersExistingUsersSpinner
            val adapter:ArrayAdapter<String>? = context?.let {
                ArrayAdapter<String>(
                    it, android.R.layout.simple_spinner_dropdown_item, nicknameList
                )
            }
            spinner.adapter = adapter

        }


        // binding.usersExistingUsersSpinner.setSelection()

        // Toggle the existing users and new user buttons to mirror each other
        binding.usersExistingUserToggleButton.setOnClickListener {
            if (binding.usersExistingUserToggleButton.isChecked) {
                binding.usersNewUserToggleButton.toggle()
            } else {
                binding.usersExistingUserToggleButton.toggle()
            }

            // Display the existing users spinner if existing users button is on
            binding.usersExistingUsersSpinner.visibility = View.VISIBLE
            binding.spinnerPromptTextView.visibility = View.VISIBLE

            // Expand the spinner automatically
            // binding.usersExistingUsersSpinner.performClick()
        }
        binding.usersNewUserToggleButton.setOnClickListener {
            if (binding.usersNewUserToggleButton.isChecked) {
                binding.usersExistingUserToggleButton.toggle()
            } else {
                binding.usersNewUserToggleButton.toggle()
            }

            // Hide the existing users spinner if new users button is on
            binding.usersExistingUsersSpinner.visibility = View.GONE
            binding.spinnerPromptTextView.visibility = View.GONE
        }

        // Go to quiz dependent on whether new or existing user
        binding.usersNextButton.setOnClickListener { v:View ->
            // If it is a new user, navigate to the patient details fragment
            if (binding.usersNewUserToggleButton.isChecked) {
                v.findNavController().navigate(R.id.action_usersFragment_to_patientDetailsFragment)

            // If it is an existing user, navigate straight to the pre-quiz fragment
            } else {
                // Send user ID to pre quiz fragment
                val nickname = binding.usersExistingUsersSpinner.selectedItem.toString()
                usersViewModel.getUserByNickname(nickname).observe(viewLifecycleOwner){ user ->
                    if (user==null){
                        Toast.makeText(context,
                            "Error, cannot find the user with the nickname",
                            Toast.LENGTH_SHORT).show()
                    }else{
                        // TODO: (Done) send the userId to the next fragment
                        // everything is good to go, navigate and pass the userId to next fragment
                        val userId = user.userId
                        val action = UsersFragmentDirections.actionUsersFragmentToPreQuizFragment(userId)
                        v.findNavController().navigate(action)

                    }
                }

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