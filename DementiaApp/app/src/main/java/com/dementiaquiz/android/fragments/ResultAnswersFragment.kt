package com.dementiaquiz.android.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentResultAnswersBinding
import com.dementiaquiz.android.fragments.adapters.QuizAnswersListAdapter
import com.dementiaquiz.android.models.ResultAnswersViewModel
import com.dementiaquiz.android.models.ResultAnswersViewModelFactory

/**
 * A fragment for display the individual answers to each question for a user
 */
class ResultAnswersFragment : Fragment() {

    private val resultAnswersViewModel: ResultAnswersViewModel by viewModels {
        ResultAnswersViewModelFactory((activity?.application as DementiaQuizApplication).quizAnswerRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentResultAnswersBinding>(
            inflater,
            R.layout.fragment_result_answers,
            container,
            false
        )

        // set up the recycleView
        val quizAnswerRecyclerview = binding.quizAnswerRecyclerview
        val adapter = QuizAnswersListAdapter()
        quizAnswerRecyclerview.adapter = adapter
        quizAnswerRecyclerview.layoutManager = LinearLayoutManager(context)


        // Get result ID argument using by navArgs property delegate
        val resultAnswersFragmentArgs by navArgs<ResultAnswersFragmentArgs>()
        val resultId = resultAnswersFragmentArgs.resultId

        resultAnswersViewModel.getQuizAnswersByResultId(resultId).observe(viewLifecycleOwner){ quizAnswerList ->

            // Update the cached copy of the nicknames in the adapter.
            quizAnswerList?.let { adapter.submitList(it) }
        }


        return binding.root
    }

}