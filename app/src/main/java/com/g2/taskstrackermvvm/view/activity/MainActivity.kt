package com.g2.taskstrackermvvm.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.g2.taskstrackermvvm.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*


class MainActivity : AppCompatActivity() {
    companion object {
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onBackPressed() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.frag_container)
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.chartsFragment,
                R.id.tagsFragment,
                R.id.updateUserProfileFragment
            ),
            main_activity_layout
        )

        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfig)

        nav_view.setupWithNavController(navController)
        nav_view.menu.findItem(R.id.sign_out_item).apply {
            val s = SpannableString(title)
            s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
            title = s

            setOnMenuItemClickListener {
                signOut()
            }
        }

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val navHeader = nav_view.getHeaderView(0)
            navHeader.nav_header_email.text = it.email
            navHeader.nav_header_display_name.text = it.displayName
        }


//        temp_log_out_btn.setOnClickListener {
//            AuthUI.getInstance().signOut(this)
//                .addOnCompleteListener {
//                    Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//                .addOnFailureListener { e ->
//                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//                }
//        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment_container)
        return NavigationUI.navigateUp(navController, main_activity_layout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController(R.id.nav_host_fragment_container)
//        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
        return super.onOptionsItemSelected(item)
    }

    private fun signOut(): Boolean {
        AuthUI.getInstance().signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            }
        return true
    }

    fun popToPreviousActivity() {
        finish()
    }

}
