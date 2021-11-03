package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentResultAnswersBinding
import com.dementiaquiz.android.databinding.FragmentResultUsersBinding
import com.dementiaquiz.android.fragments.adapters.QuizAnswersListAdapter
import com.dementiaquiz.android.fragments.adapters.UserListAdapter
import com.dementiaquiz.android.models.QuizResultViewModel
import com.dementiaquiz.android.models.QuizResultViewModelFactory
import com.dementiaquiz.android.models.ResultAnswersViewModel
import com.dementiaquiz.android.models.ResultAnswersViewModelFactory


class ResultAnswersFragment : Fragment() {


    //TODO: this may crash the app, there might be another better solution
    // see "https://stackoverflow.com/questions/11585702/how-to-get-application-object-into-fragment-class"
    private val resultAnswersViewModel: ResultAnswersViewModel by viewModels {
        ResultAnswersViewModelFactory((activity?.application as DementiaQuizApplication).quizAnswerRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        //TODO: fetch ID
        resultAnswersViewModel.getQuizAnswersByResultId(resultId).observe(viewLifecycleOwner){ quizAnswerList ->

            // Update the cached copy of the nicknames in the adapter.
            quizAnswerList?.let { adapter.submitList(it) }
        }


        return binding.root
    }

}