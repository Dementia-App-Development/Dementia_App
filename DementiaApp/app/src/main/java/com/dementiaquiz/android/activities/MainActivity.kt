package com.dementiaquiz.android.activities

import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI

import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.ActivityMainBinding
import timber.log.Timber

//TODO: can probably remove this constant, unless when we are loading quiz from file after onStop/onResume in fragment
const val QUIZ_QUESTIONS_JSON = "quiz_questions.json"

/**
 * Main activity for the Dementia Quiz App, hosts a number of fragments
 */
class MainActivity : AppCompatActivity() {

    // Creating a binding object for the main_activity.xml layout
    private lateinit var binding: ActivityMainBinding

    // create a reference to drawerLayout
    private lateinit var drawerLayout: DrawerLayout


    // Initialize the current question index to 0 (the first question)
    // TODO: probably need to store this persistent data variable somewhere else
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState)

        Timber.i("onCreate called")

        // use DataBindingUtil to set the content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root
        setContentView(view)

        // get drawerLayout
        drawerLayout = binding.drawerLayout


        // connect navController with actionBar
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this,navController,drawerLayout)

        // display the navigation drawer
        NavigationUI.setupWithNavController(binding.navView, navController)

    }
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }

}