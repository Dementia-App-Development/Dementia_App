package com.dementiaquiz.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
 * Displays the historical data obtained from results from the quiz by each user
 */
class ResultUsersFragment : Fragment() {

    private val usersViewModel: UsersViewModel by viewModels {
        UsersViewModelFactory((activity?.application as DementiaQuizApplication).userRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        Timber.i("onCreateView called")

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

//        Timber.i("recycleView adapter attached")

        usersViewModel.getAllUsers().observe(viewLifecycleOwner){ nickNameList ->

            // Update the cached copy of the nicknames in the adapter.
            nickNameList?.let { adapter.submitList(it) }
        }

        return binding.root
    }

    private fun onListItemClick(userId: Long) {
//        Timber.i("The user ID of the clicked item is: $userId")

        // Pass the user ID argument and navigate to the resultResultsFragment
        val action = ResultUsersFragmentDirections.actionResultUsersFragmentToResultResultsFragment(userId)
        view?.findNavController()?.navigate(action)
    }

}