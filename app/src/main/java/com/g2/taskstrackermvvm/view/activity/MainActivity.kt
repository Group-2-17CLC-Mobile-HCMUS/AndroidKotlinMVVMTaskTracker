package com.g2.taskstrackermvvm.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.view.fragment.SignInFragment
import com.g2.taskstrackermvvm.view.fragment.TestFragment



class MainActivity : AppCompatActivity(), SignInFragment.IOnSignedIn {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()
        supportFragmentManager.commit {
            add(R.id.frag_container, SignInFragment())
        }
    }

    override fun onSignedIn(uid: String) {
//        val homeScreenIntent = HomeScreenActivity.newInstance(this)
//        startActivity(homeScreenIntent)

        supportFragmentManager.commit {
            replace(R.id.frag_container, TestFragment())
        }
    }

}
