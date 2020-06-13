package com.g2.taskstrackermvvm.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.viewmodel.MainActivityViewModel
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.firebase.auth.FirebaseAuth
import com.yariksoffice.lingver.Lingver
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity() {

    private var darkTheme by Delegates.notNull<Boolean>()
    private val viewModel: MainActivityViewModel by viewModel()
    override fun onBackPressed() {
    }

    override fun onPause() {
        super.onPause()
        getSharedPreferences("Setting", Context.MODE_PRIVATE).edit {
            putBoolean(
                DARK_THEME_BUNDLE_KEY,
                darkTheme
            )
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        initTheme(savedInstanceState)
        initLanguage(savedInstanceState)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val navController = findNavController(R.id.frag_container)
        val appBarConfig = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.chartsFragment,
                R.id.tagsFragment,
                R.id.updateUserProfileFragment,
                R.id.switch_theme
            ),
            main_activity_layout
        )

        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfig)

        nav_view.setupWithNavController(navController)
        nav_view.menu.apply {
            findItem(R.id.sign_out_item).apply {
                val s = SpannableString(title)
                s.setSpan(ForegroundColorSpan(Color.RED), 0, s.length, 0)
                title = s

                setOnMenuItemClickListener {
                    signOut()
                }
            }

            findItem(R.id.switch_theme_item).apply {
                val switch = this.actionView.findViewById<SwitchMaterial>(R.id.switch_theme)
                Log.d(
                    "com.g2.taskstrackermvvm.view.activity.MainActivity",
                    darkTheme.toString()
                )
                switch?.isChecked = darkTheme
                switch?.setOnCheckedChangeListener { _, isChecked ->
                    darkTheme = isChecked
                    if (darkTheme)
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }

            findItem(R.id.change_language_item).apply {

                setOnMenuItemClickListener {
                    setNewLocale("en")
                }
            }
        }


        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val navHeader = nav_view.getHeaderView(0)
            navHeader.nav_header_email.text = it.email
            navHeader.nav_header_display_name.text = it.displayName
        }

    }

    private fun initTheme(savedInstanceState: Bundle?) {
        darkTheme = getSharedPreferences("Setting", Context.MODE_PRIVATE).getBoolean(
            DARK_THEME_BUNDLE_KEY,
            false
        )
        savedInstanceState?.let {
            darkTheme = it.getBoolean(DARK_THEME_BUNDLE_KEY)
        }
        if (darkTheme)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(DARK_THEME_BUNDLE_KEY, darkTheme)
        super.onSaveInstanceState(outState)
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

    private fun initLanguage(savedInstanceState: Bundle?) {
        val lingverLang = Lingver.getInstance().getLanguage()
        val english = Locale(lingverLang).language.equals("en", true)

        if (english) {
            language.text = resources.getString(R.string.english)
        } else {
            language.text = resources.getString(R.string.vi)
        }
        language.setOnClickListener {
            if (english) {
                setNewLocale("vi")
                language.text = resources.getString(R.string.vi)
            } else {
                setNewLocale("en")
                language.text = resources.getString(R.string.english)
            }
        }
    }

    private fun setNewLocale(language: String, country: String = ""):Boolean {
        Lingver.getInstance().setLocale(this, language, country)
        Lingver.getInstance().setFollowSystemLocale(this)
        restart()
        return true
    }

    private fun restart() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))

        Toast.makeText(this, "Activity restarted", Toast.LENGTH_SHORT).show()
    }

    private fun signOut(): Boolean {
        AuthUI.getInstance().signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this, "Signed Out", Toast.LENGTH_SHORT).show()
                viewModel.cleanUp()
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

    companion object {
        const val TAG = "com.g2.taskstrackermvvm.view.activity.MainActivity"
        const val DARK_THEME_BUNDLE_KEY = "com.g2.taskstrackermvvm.view.activity.MainActivity"
        fun startActivity(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

}
