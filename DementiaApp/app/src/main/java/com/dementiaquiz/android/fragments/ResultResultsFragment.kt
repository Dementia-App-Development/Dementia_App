package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.dementiaquiz.android.R
import timber.log.Timber


class ResultResultsFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // Get user ID argument using by navArgs property delegate
        val resultResultsFragmentArgs by navArgs<ResultResultsFragmentArgs>()
        val userId = resultResultsFragmentArgs.userId

        Timber.i("The passed userId is: $userId")

        return inflater.inflate(R.layout.fragment_result_results, container, false)
    }

}