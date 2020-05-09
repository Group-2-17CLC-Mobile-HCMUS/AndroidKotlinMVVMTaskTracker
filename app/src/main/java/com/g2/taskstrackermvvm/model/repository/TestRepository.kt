package com.g2.taskstrackermvvm.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

interface ITestRepo {
    fun basicReadWrite(): LiveData<String>
}

class TestRepositoryImp : ITestRepo {
    companion object {
        private const val TAG = "KotlinActivity"
    }

    private val data: MutableLiveData<String> = MutableLiveData()

    override fun basicReadWrite(): LiveData<String> {
        // [START write_message]
        // Write a message to the database
        val database = Firebase.database
        Log.d(TAG, "Db is: $database")
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!" + UUID.randomUUID())
        // [END write_message]

        // [START read_message]
        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                data.value = dataSnapshot.value as String
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
        // [END read_message]

        return data
    }

}