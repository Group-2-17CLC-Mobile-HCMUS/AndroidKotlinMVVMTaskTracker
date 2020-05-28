package com.g2.taskstrackermvvm.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.view.fragment.SignInFragment

class SignInActivity : AppCompatActivity(), SignInFragment.IOnSignIn {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
    }

    override fun onSignIn() {
        MainActivity.startActivity(this)
    }
}
