package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentResultsBinding
import com.dementiaquiz.android.fragments.adapters.UserListAdapter
import timber.log.Timber

/**
 * Displays the historical data obtained from results from the quiz
 */
class ResultsFragment : Fragment() {

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


        // TODO change to nav host controller binding

        return binding.root
    }

}