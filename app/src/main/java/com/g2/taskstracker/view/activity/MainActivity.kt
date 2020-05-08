package com.g2.taskstracker.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.g2.taskstracker.R
import com.g2.taskstracker.view.fragment.SignInFragment
import com.g2.taskstracker.view.fragment.TestFragment


class MainActivity : AppCompatActivity(), SignInFragment.IOnSignedIn {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.commit {
            add(R.id.frag_container, SignInFragment())
        }
    }

    override fun onSignedIn(uid: String) {
        supportFragmentManager.commit {
            replace(R.id.frag_container, TestFragment())
        }
    }

}
