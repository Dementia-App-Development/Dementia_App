package com.dementiaapp.quiz.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dementiaapp.quiz.R

/**
 * Displays the historical data obtained from results from the quiz
 */
class resultsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // TODO change to nav host controller binding
        return inflater.inflate(R.layout.fragment_results, container, false)
    }

}