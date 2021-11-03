package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentResultsBinding
import com.dementiaquiz.android.fragments.adapters.UserListAdapter
import com.dementiaquiz.android.models.UsersViewModel
import com.dementiaquiz.android.models.UsersViewModelFactory
import timber.log.Timber

/**
 * Displays the historical data obtained from results from the quiz
 */
class ResultsFragment : Fragment() {

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
        val binding = DataBindingUtil.inflate<FragmentResultsBinding>(
            inflater,
            R.layout.fragment_results,
            container,
            false
        )


        // set up the recycleView
        val userRecyclerView = binding.userRecyclerview
        val adapter = UserListAdapter()
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

}