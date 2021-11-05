package com.dementiaquiz.android.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentUsersBinding
import com.dementiaquiz.android.models.UsersViewModel
import com.dementiaquiz.android.models.UsersViewModelFactory

/**
 * A fragment that prompts the user if they are a new user, or an existing one
 * Passes the user ID to the next fragment
 */
class UsersFragment : Fragment() {

    private val usersViewModel: UsersViewModel by viewModels {
        UsersViewModelFactory((activity?.application as DementiaQuizApplication).userRepository)
    }

    private lateinit var binding : FragmentUsersBinding

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        // Use view binding to get variables from XML
        binding = DataBindingUtil.inflate<FragmentUsersBinding>(inflater,R.layout.fragment_users, container, false)

        // Populate existing users spinner with the nicknames from the user database once there are
        // change on the user table on the run time
        usersViewModel.getAllNicknames().observe(viewLifecycleOwner){ nicknameList->

            val spinner = binding.usersExistingUsersSpinner

            if (nicknameList.isEmpty()) {
                spinner.visibility = View.GONE
                binding.usersNextButton.visibility = View.GONE
                binding.spinnerPromptTextView.text = "No users found. Please register new user"
                binding.usersHeaderTextView.text = "Register"
            } else {

                val adapter: ArrayAdapter<String>? = context?.let {
                    ArrayAdapter<String>(
                        it, android.R.layout.simple_spinner_dropdown_item, nicknameList
                    )
                }
                spinner.adapter = adapter
            }

        }

        binding.usersNextButton.setOnClickListener { v:View ->
            // Send user ID to pre quiz fragment
            val nickname = binding.usersExistingUsersSpinner.selectedItem.toString()
            usersViewModel.getUserByNickname(nickname).observe(viewLifecycleOwner){ user ->
                if (user==null){
                    Toast.makeText(context,
                        "Error, cannot find the user with the nickname",
                        Toast.LENGTH_SHORT).show()
                }else{
                    // Navigate and pass the userId to next fragment
                    val userId = user.userId
                    val action = UsersFragmentDirections.actionUsersFragmentToPreQuizFragment(userId)
                    v.findNavController().navigate(action)

                }
            }
        }

        binding.usersRegisterButton.setOnClickListener { v:View ->
            v.findNavController().navigate(R.id.action_usersFragment_to_patientDetailsFragment)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

}