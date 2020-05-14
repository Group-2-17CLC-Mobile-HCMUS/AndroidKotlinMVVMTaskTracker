package com.g2.taskstrackermvvm.model.repository

import android.util.Log
import com.g2.taskstrackermvvm.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.HashMap

interface IUserRepo {
    fun addUser()
    fun updateUser(newPoint: Int)
}

class UserRepositoryImp : IUserRepo {
    companion object {
        private const val TAG = "com.g2.taskstrackermvvm.model.repository.user"
    }

    override fun addUser() {
        val database = Firebase.database
        val userRef = database.getReference("users")

        val user = FirebaseAuth.getInstance().currentUser?.email?.let { User(it, FirebaseAuth.getInstance().currentUser?.uid.toString()) }

        if (user != null) {
            userRef.child(user.uid).addListenerForSingleValueEvent(object : ValueEventListener { // goi lan dau -> trigger onDataChange -> check data user.uid = snapshot
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        userRef.child(user.uid).setValue(user)
                    }
                }
            })
        }
    }

    override fun updateUser(newPoint: Int) {
        val database = Firebase.database
        val userRef = database.getReference("users")

        val user = FirebaseAuth.getInstance().currentUser?.email?.let { User(it, FirebaseAuth.getInstance().currentUser?.uid.toString()) }

        if (user != null) {
            userRef.child("${user.uid}/points").setValue(newPoint)
        }
    }

}