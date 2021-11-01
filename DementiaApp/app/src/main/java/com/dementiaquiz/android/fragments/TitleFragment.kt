package com.dementiaquiz.android.fragments

import android.Manifest
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi

import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.FragmentTitleBinding
import timber.log.Timber

/**
 * Home screen menu when first opening the app, contains main buttons to navigate the app
 */
class TitleFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Prompt the user for location permission
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate view binding to get variables from XML
        val binding = DataBindingUtil.inflate<FragmentTitleBinding>(
            inflater, R.layout.fragment_title, container, false
        )

        // Use Nav Controller to set quiz button to navigate to quiz fragment
        binding.titleQuizButton.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_titleFragment_to_preQuizFragment)
        }

        // Use Nav Controller to set results button to navigate to results fragment
        binding.titleResultsButton.setOnClickListener { v: View ->
            v.findNavController().navigate(R.id.action_titleFragment_to_resultsFragment)
        }

        // Go to Dementia Australia Website
        binding.titleWebsiteButton.setOnClickListener { v: View ->
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
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController())
                || super.onOptionsItemSelected(item)
    }

}