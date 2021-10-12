package com.dementiaquiz.android.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import android.content.Intent
import android.net.Uri

import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentTitleBinding
import timber.log.Timber

/**
 * Home screen menu when first opening the app
 */
class TitleFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Timber.i("onCreateView called")

        // Use view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(inflater,
            R.layout.fragment_title, container, false)

        // Use Nav Controller to set quiz button to navigate to quiz fragment
        binding.btnQuiz.setOnClickListener { v: View ->
            v.findNavController().navigate(com.dementiaquiz.android.fragments.TitleFragmentDirections.actionTitleFragmentToPreQuizFragment2())
        }

        // Use Nav Controller to set results button to navigate to results fragment
        binding.btnResults.setOnClickListener { v: View ->
            v.findNavController()
                .navigate(com.dementiaquiz.android.fragments.TitleFragmentDirections.actionTitleFragmentToResultsFragment())
        }

        binding.websiteButton.setOnClickListener { v:View->

            val uri: Uri =
                Uri.parse("https://www.dementia.org.au/") // missing 'http://' will cause crashed

            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.
        onNavDestinationSelected(item,requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

}