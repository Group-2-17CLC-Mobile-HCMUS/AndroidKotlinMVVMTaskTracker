package com.g2.taskstrackermvvm.view.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.g2.taskstrackermvvm.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.frag_container)
        val appBarConfig = AppBarConfiguration(setOf(R.id.testFragment))
        toolbar.setupWithNavController(navController, appBarConfig)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.signInFragment -> toolbar.visibility = View.GONE
                else -> toolbar.visibility = View.VISIBLE
            }
        }
    }


}
