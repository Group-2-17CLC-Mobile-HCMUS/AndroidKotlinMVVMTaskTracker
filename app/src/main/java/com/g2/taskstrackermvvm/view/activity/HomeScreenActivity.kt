package com.g2.taskstrackermvvm.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.g2.taskstrackermvvm.R
import com.g2.taskstrackermvvm.view.fragment.CalendarFragment
import com.g2.taskstrackermvvm.view.fragment.HomeListFragment

class HomeScreenActivity : AppCompatActivity() {

    companion object {
        fun newInstance(context: Activity) : Intent {
            return Intent(context, HomeScreenActivity::class.java)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        supportFragmentManager.commit {
            add(R.id.home_frag_calendar_container, CalendarFragment())
            add(R.id.home_task_list_frag_container, HomeListFragment())
        }
    }
}
