package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dementiaquiz.android.R
import com.dementiaquiz.android.models.ResultsUserAdapter
import timber.log.Timber
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.dementiaquiz.android.databinding.FragmentResultsBinding

/**
 * Displays the historical data obtained from results from the quiz
 */
class ResultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentResultsBinding>(inflater, R.layout.fragment_results, container, false)

        val adapter = ResultsUserAdapter()
        binding.resultUserList.adapter = adapter

        Timber.i("onCreateView called")
        return binding.root
    }
}