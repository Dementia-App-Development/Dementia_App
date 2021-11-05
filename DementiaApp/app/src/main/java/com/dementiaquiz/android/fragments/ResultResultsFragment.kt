package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.dementiaquiz.android.DementiaQuizApplication
import com.dementiaquiz.android.R
import com.dementiaquiz.android.database.model.QuizResult
import com.dementiaquiz.android.databinding.FragmentResultResultsBinding
import com.dementiaquiz.android.databinding.FragmentResultUsersBinding
import com.dementiaquiz.android.fragments.adapters.QuizResultAdapter
import com.dementiaquiz.android.fragments.adapters.UserListAdapter
import com.dementiaquiz.android.models.QuizResultViewModel
import com.dementiaquiz.android.models.QuizResultViewModelFactory
import com.dementiaquiz.android.models.UsersViewModel
import com.dementiaquiz.android.models.UsersViewModelFactory
import timber.log.Timber

/**
 * A fragment for display results
 */
class ResultResultsFragment : Fragment() {

    private val quizResultViewModel: QuizResultViewModel by viewModels {
        QuizResultViewModelFactory((activity?.application as DementiaQuizApplication).quizResultRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentResultResultsBinding>(
            inflater,
            R.layout.fragment_result_results,
            container,
            false
        )

        // set up the recycleView
        val quizResultRecyclerView = binding.quizResultRecyclerview
        val adapter = QuizResultAdapter{ resultId -> onListItemClick(resultId)}
        quizResultRecyclerView.adapter = adapter
        quizResultRecyclerView.layoutManager = LinearLayoutManager(context)

        // Get user ID argument using by navArgs property delegate
        val resultResultsFragmentArgs by navArgs<ResultResultsFragmentArgs>()
        val userId = resultResultsFragmentArgs.userId

//        Timber.i("The passed userId is: $userId")

        quizResultViewModel.getQuizResultsByUserId(userId).observe(viewLifecycleOwner){ quizResultList ->

            // Update the cached copy of the quizResults in the adapter.
            quizResultList?.let { adapter.submitList(it) }

        }

        return binding.root
    }

    private fun onListItemClick(resultId: Long) {

//        Timber.i("The resultId of the clicked item is: $resultId")

        // Navigate to the next fragment and passing the resultId
        val action = ResultResultsFragmentDirections.actionResultResultsFragmentToResultAnswersFragment(resultId)
        view?.findNavController()?.navigate(action)

    }

}