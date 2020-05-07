package com.g2.taskstracker.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.g2.taskstracker.R
import com.g2.taskstracker.view.fragment.TestFragment


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().add(R.id.frag_container, TestFragment()).commit()
    }

}
