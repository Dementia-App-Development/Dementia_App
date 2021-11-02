package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentResultsBinding
import com.dementiaquiz.android.models.QuizViewModel
import com.dementiaquiz.android.models.ResultsViewModel
import timber.log.Timber

/**
 * Displays the historical data obtained from results from the quiz
 */
class ResultsFragment : Fragment() {

    private lateinit var listView: ListView
    private lateinit var resultsViewModel: ResultsViewModel

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.i("onCreateView called")

        resultsViewModel = ViewModelProvider(requireActivity()).get(ResultsViewModel::class.java)
        resultsViewModel.setUserRepository((activity?.application as DementiaQuizApplication).userRepository)

        Timber.i("resultsViewModel provider stuff")

        val binding = DataBindingUtil.inflate<FragmentResultsBinding>(
            inflater, R.layout.fragment_results, container, false
        )

        Timber.i("binding")

        listView = binding.userListResultsView



        resultsViewModel.getAllUsers().observe(viewLifecycleOwner) { usersList ->
            Timber.i("Before getting size")
            val listItems = arrayOfNulls<String>(usersList.size)

            Timber.i("Right before getting nicknames")
// 3

            for (i in usersList.indices) {
                val user = usersList[i]
                listItems[i] = user.nickname
            }

            Timber.i("Right before adapter")
            val adapter = ArrayAdapter(resultsViewModel.context, android.R.layout.simple_list_item_1,listItems)
            listView.adapter = adapter
            listView.refreshDrawableState()
        }

// 2


        // Inflate the layout for this fragment
        // TODO change to nav host controller binding
        return binding.root
    }

}