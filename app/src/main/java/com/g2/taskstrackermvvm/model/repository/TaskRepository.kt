package com.g2.taskstrackermvvm.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.g2.taskstrackermvvm.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

interface ITaskRepo {
    fun addTask(title: String, desc: String, priority: Task.Priority, created: Date, dueDate: Date)
    fun getListTask() : LiveData<List<Task>>
}

class TaskRepositoryImp : ITaskRepo {
    companion object {
        private const val TAG = "com.g2.taskstrackermvvm.model.repository.task"
    }

    private val listTask: MutableLiveData<List<Task>> = MutableLiveData()

    override fun addTask(title: String, desc: String, priority: Task.Priority, created: Date, dueDate: Date) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")

        val task = Task(title, desc, priority, created, dueDate)
        if (user != null) {
            taskRef.child(user.uid).push().setValue(task)
        }

    }

    override fun getListTask() : LiveData<List<Task>> {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val list = mutableListOf<Task>()

        val taskRef = database.getReference("tasks")
        val taskUidRef = user?.uid?.let { taskRef.child(it) }
        taskUidRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for ( taskSnapshot in dataSnapshot.children ) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        list.add(task)
                    }
                }
                listTask.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })

        return listTask
    }
}