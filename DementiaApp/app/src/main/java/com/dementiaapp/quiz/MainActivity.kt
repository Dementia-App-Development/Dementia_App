package com.dementiaapp.quiz

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.dementiaapp.quiz.databinding.ActivityMainBinding

const val QUIZ_QUESTIONS_JSON = "quiz_questions.json"

class MainActivity : AppCompatActivity() {

    // Creating a binding object for the main_activity.xml layout
    private lateinit var binding: ActivityMainBinding

    // create a reference to drawerLayout
    private lateinit var drawerLayout: DrawerLayout


    // Initialize the current question index to 0 (the first question)
    private var currentQuestionIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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