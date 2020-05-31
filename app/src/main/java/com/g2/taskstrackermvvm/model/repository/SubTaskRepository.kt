package com.g2.taskstrackermvvm.model.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.g2.taskstrackermvvm.model.SubTask
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

interface ISubTaskRepo {
    fun addSubTask(name: String, taskId: String)
    fun getListSubTask(taskId: String): LiveData<List<SubTask>>
    fun removeSubTask(taskId: String, subTaskId: String)
    fun updateTask(newSubTask: SubTask, subTaskId: String, taskId: String)
}

class SubTaskRepositoryImp : ISubTaskRepo {

    companion object {
        const val TAG = "com.g2.taskstrackermvvm.model.repository.SubTaskRepositoryImp"
    }

    private val listSubTask: MutableLiveData<List<SubTask>> = MutableLiveData()

    override fun addSubTask(name: String, taskId: String) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        //val userID = user.uid
        val subTaskRef = database.getReference("tasks/${user?.uid}/$taskId/subTasks")


        if (user != null) {
            val singleSubTaskRef = subTaskRef.child(user.uid).child(taskId).push()
            val subTask = singleSubTaskRef.key?.let { SubTask(it, name) }
        }
    }

    override fun getListSubTask(taskId: String): LiveData<List<SubTask>> {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val list = mutableListOf<SubTask>()

        val taskRef = database.getReference("tasks")
        val subTaskUidRef = user?.uid?.let { taskRef.child(it).child(taskId) }
        subTaskUidRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (subTaskSnapshot in dataSnapshot.children) {
                    val subTask = subTaskSnapshot.getValue(SubTask::class.java)
                    subTask?.let {
                        subTaskSnapshot.key?.let { it1 ->
                            it.id = it1
                            list.add(it)
                        }
                    }
                }
                listSubTask.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w(TAG, "db error ${error.toException()}")
            }
        })

        return listSubTask
    }

    override fun removeSubTask(taskId: String, subTaskId: String) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")
        //val userID = user.uid
        val subTaskRef = database.getReference("tasks/${user?.uid}/$taskId/subTasks")

        if (user != null) {
            subTaskRef.child(subTaskId).setValue(null)
        }
    }

    override fun updateTask(newSubTask: SubTask, subTaskId: String, taskId: String) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val subTaskRef = database.getReference("tasks/${user?.uid}/$taskId/subTasks")
        //

        if (user != null) {
            val childSubTaskRef = subTaskRef.child(subTaskId)
            childSubTaskRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled ${databaseError.message}")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        childSubTaskRef.child("name").setValue(newSubTask.name)
                        childSubTaskRef.child("status").setValue(newSubTask.status)
                    }
                }
            })
        }
    }

}