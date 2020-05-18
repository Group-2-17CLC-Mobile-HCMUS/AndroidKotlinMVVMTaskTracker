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
    fun updateTask(newTask: Task)
    fun getTaskDetail(taskId: String) : LiveData<Task>
    fun getListTask() : LiveData<List<Task>>
    fun setTag(taskId: String, tagId: String)
    fun removeTag(taskId: String, tagId: String)
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

        val task = Task("",title, desc, priority, created, dueDate)
        if (user != null) {
            taskRef.child(user.uid).push().setValue(task)
        }
    }

    override fun updateTask(newTask: Task) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")

        if (user != null) {
            val childTaskRef = taskRef.child(user.uid).child(newTask.id)
            childTaskRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (!snapshot.exists()) {
                        childTaskRef.child("title").setValue(newTask.title)
                        childTaskRef.child("desc").setValue(newTask.desc)
                        childTaskRef.child("priority").setValue(newTask.priority)
                        childTaskRef.child("status").setValue(newTask.status)
                        childTaskRef.child("dueDate").setValue(newTask.dueDate)
                    }
                }
            })
        }
    }

    override fun getTaskDetail(taskId: String): LiveData<Task> {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")
        val taskDetail : MutableLiveData<Task> = MutableLiveData()

        if (user != null) {
            val childTaskRef = taskRef.child(user.uid).child(taskId)
            childTaskRef.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }

                override fun onDataChange(taskSnapshot: DataSnapshot) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        taskDetail.value = task
                    }
                }
            })
        }
        return taskDetail
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
                        task.id = taskSnapshot.key.toString()
                        for (tag in taskSnapshot.child("tags").children) {
                            tag.key?.let { task.addTag(it) }
                        }
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

    override fun setTag(taskId: String, tagId: String) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")
        val tagRef = database.getReference("tags")

        if (user != null) {
            val task = taskRef.child(user.uid).child(taskId)
            task.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        task.child("tags").child(tagId).setValue(true)
                    }
                }

            })
            val tag = tagRef.child(user.uid).child(tagId)
            tag.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        tag.child("tasks").child(taskId).setValue(true)
                    }                }
            })
        }

    }

    override fun removeTag(taskId: String, tagId: String) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")
        val tagRef = database.getReference("tags")

        if (user != null) {
            taskRef.child(user.uid).child(taskId).child(tagId).setValue(null)
            tagRef.child(user.uid).child(tagId).child(taskId).setValue(null)
        }
    }


}