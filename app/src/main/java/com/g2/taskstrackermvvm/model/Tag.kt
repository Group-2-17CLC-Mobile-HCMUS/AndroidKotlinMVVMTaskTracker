package com.g2.taskstrackermvvm.model

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.FirebaseDatabase

data class Tag(val id: String, val name: String, val color: Color, val bindedTask: List<String>?) {

    enum class Color(val rbg: Int) {
        RED(0xFF0000),
        GREEN(0x00FF00),
        BLUE(0x0000FF)
    }
}