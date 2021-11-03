package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentResultUsersBinding
import com.dementiaquiz.android.fragments.adapters.UserListAdapter
import com.dementiaquiz.android.models.UsersViewModel
import com.dementiaquiz.android.models.UsersViewModelFactory
import timber.log.Timber

/**
 * Displays the historical data obtained from results from the quiz
 */
class ResultUsersFragment : Fragment() {

    //TODO: this may crash the app, there might be another better solution
    // see "https://stackoverflow.com/questions/11585702/how-to-get-application-object-into-fragment-class"
    private val usersViewModel: UsersViewModel by viewModels {
        UsersViewModelFactory((activity?.application as DementiaQuizApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.i("onCreateView called")

        // Inflate view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentResultUsersBinding>(
            inflater,
            R.layout.fragment_result_users,
            container,
            false
        )


        // set up the recycleView
        val userRecyclerView = binding.userRecyclerview
        val adapter = UserListAdapter{userId -> onListItemClick(userId)}
        userRecyclerView.adapter = adapter
        userRecyclerView.layoutManager = LinearLayoutManager(context)

        Timber.i("recycleView adapter attached")

        usersViewModel.getAllUsers().observe(viewLifecycleOwner){ nickNameList ->

            // Update the cached copy of the words in the adapter.
            nickNameList?.let { adapter.submitList(it) }
        }


        // TODO change to nav host controller binding

        return binding.root
    }

    private fun onListItemClick(userId: Long) {
//        Toast.makeText(context, "I am clicked: $position", Toast.LENGTH_SHORT).show()
        Timber.i("The user ID of the clicked item is: $userId")

        //TODO: navigate to the next fragment and passing the userId

        // Pass the user ID argument and navigate to the resultResultsFragment
//        val action = PreQuizFragmentDirections.actionPreQuizFragmentToQuizFragment(userID)
        val action = ResultUsersFragmentDirections.actionResultUsersFragmentToResultResultsFragment(userId)
        view?.findNavController()?.navigate(action)
    }

}