package com.dementiaquiz.android.activities

import android.content.res.Configuration
import android.os.Bundle
import android.text.Html
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.dementiaquiz.android.R
import com.dementiaquiz.android.databinding.ActivityMainBinding

/**
 * Main activity for the Dementia Quiz App, hosts a number of fragments
 */
class MainActivity : AppCompatActivity() {

    // Creating a binding object for the main_activity.anim layout
    private lateinit var binding: ActivityMainBinding

    // create a reference to drawerLayout
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)

//        Timber.i("onCreate called")

        // use DataBindingUtil to set the content view
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val view = binding.root
        setContentView(view)

        // Change the title colour
        val nightModeFlags: Int = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            // Change the colour of the action bar to same as colour theme
            supportActionBar?.title = (Html.fromHtml("<font color=\"#003349\">" + "Dementia App" + "</font>"))
        }
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