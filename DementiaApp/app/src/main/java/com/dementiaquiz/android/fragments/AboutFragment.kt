package com.dementiaquiz.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.dementiaquiz.android.R

/**
 * TODO: fragment description
 */
class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        /*// set the title of the actionBar
        (activity as AppCompatActivity).supportActionBar?.title = "About"*/


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false)


    }
}