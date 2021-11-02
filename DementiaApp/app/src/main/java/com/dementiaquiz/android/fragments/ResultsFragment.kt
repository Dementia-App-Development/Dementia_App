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
import com.dementiaquiz.android.databinding.FragmentQuizBinding
import com.dementiaquiz.android.databinding.FragmentResultsBinding

/**
 * Displays the historical data obtained from results from the quiz
 */
class ResultsFragment : Fragment() {

    private lateinit var binding: FragmentResultsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.i("onCreateView called")
        return inflater.inflate(R.layout.fragment_results, container, false)
    }
}