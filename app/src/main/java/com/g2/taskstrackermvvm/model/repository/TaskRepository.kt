package com.g2.taskstrackermvvm.model.repository

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.g2.taskstrackermvvm.model.SubTask
import com.g2.taskstrackermvvm.model.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

interface ITaskRepo {
    fun createTask(newTask: Task)
    fun addTask(title: String, desc: String, priority: Task.Priority, created: Date, dueDate: Date)
    fun updateTask(newTask: Task)
    fun getTaskDetail(taskId: String): LiveData<Task>
    fun clearSingleTask(taskId: String)
    fun getListTask(): LiveData<List<Task>>
    fun setTag(taskId: String, tagId: String)
    fun removeTag(taskId: String, tagId: String)
    fun exportCalendar(context: Context, taskID: String)
    fun removeTask(task: Task)
    fun cleanUp()
}

class TaskRepositoryImp : ITaskRepo {

    private val listTask: MutableLiveData<List<Task>> = MutableLiveData()
    private var isListFetched = false
    private val listenerTrackMap: MutableMap<Query, ValueEventListener> = mutableMapOf()
    override fun createTask(newTask: Task) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")

        if (user != null) {
            val newTaskId = taskRef.child(user.uid).push().key
            newTaskId?.let { taskId ->
                newTask.id = newTaskId
                taskRef.child(user.uid).child(taskId).setValue(newTask)
                newTask.tagIds.forEach { tagId ->
                    taskRef.child(user.uid).child(taskId).child("tags").child(tagId).setValue(true)
                }
                newTask.subTasks.forEach { subtask ->
                    taskRef.child(user.uid).child(taskId).child("subTasks").push().key?.let {
                        subtask.id = it
                        taskRef.child(user.uid).child(taskId).child("subTasks").child(subtask.id)
                            .setValue(subtask)
                    }
                }
            }
        }
    }

    override fun addTask(
        title: String,
        desc: String,
        priority: Task.Priority,
        created: Date,
        dueDate: Date
    ) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")

        val task = Task("", title, desc, priority, created, dueDate)
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
            childTaskRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
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
        val taskDetail: MutableLiveData<Task> = MutableLiveData()

        if (user != null) {
            val childTaskRef = taskRef.child(user.uid).child(taskId)
            val taskListener = object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }

                override fun onDataChange(taskSnapshot: DataSnapshot) {
                    val task = taskSnapshot.getValue(Task::class.java)
                    if (task != null) {
                        task.id = taskSnapshot.key.toString()
                        for (tag in taskSnapshot.child("tags").children) {
                            tag.key?.let { task.addTag(it) }
                        }
                        for (subtask in taskSnapshot.child("subTasks").children) {
                            subtask.key?.let {
                                subtask.getValue(SubTask::class.java)?.let { it1 ->
                                    it1.id = it
                                    task.addSubtask(
                                        it1
                                    )
                                }
                            }
                        }
                        taskDetail.value = task
                    }
                }
            }

            listenerTrackMap[childTaskRef] = taskListener
            childTaskRef.addValueEventListener(taskListener)
        }
        return taskDetail
    }

    override fun clearSingleTask(taskId: String) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")
        if (user != null) {
            val childTaskRef = taskRef.child(user.uid).child(taskId)
            listenerTrackMap[childTaskRef]?.let {
                childTaskRef.removeEventListener(it)
                listenerTrackMap.remove(childTaskRef)
            }
        }
    }

    override fun getListTask(): LiveData<List<Task>> {
        if (isListFetched)
            return listTask
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val list = mutableListOf<Task>()
        val taskRef = database.getReference("tasks")
        if (user != null) {
            val taskUidRef = taskRef.child(user.uid)
            val tasksListListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    list.clear()
                    for (taskSnapshot in dataSnapshot.children) {
                        val task = taskSnapshot.getValue(Task::class.java)
                        if (task != null) {
                            task.id = taskSnapshot.key.toString()
                            for (tag in taskSnapshot.child("tags").children) {
                                tag.key?.let { task.addTag(it) }
                            }
                            for (subtask in taskSnapshot.child("subTasks").children) {
                                subtask.key?.let {
                                    subtask.getValue(SubTask::class.java)?.let { it1 ->
                                        it1.id = it
                                        task.addSubtask(
                                            it1
                                        )
                                    }
                                }
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
            }

            listenerTrackMap[taskUidRef] = tasksListListener
            taskUidRef.addValueEventListener(tasksListListener)

            isListFetched = true
        }

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
            tag.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(databaseError: DatabaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        tag.child("tasks").child(taskId).setValue(true)
                    }
                }
            })
        }

    }

    override fun removeTag(taskId: String, tagId: String) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")
        val tagRef = database.getReference("tags")

        if (user != null) {
            taskRef.child(user.uid).child(taskId).child("tags").child(tagId).setValue(null)
            tagRef.child(user.uid).child(tagId).child("tasks").child(taskId).setValue(null)
        }
    }

    override fun removeTask(task: Task) {
        val database = Firebase.database
        val user = FirebaseAuth.getInstance().currentUser
        val taskRef = database.getReference("tasks")
        val tagRef = database.getReference("tags")

        if (user != null) {
            taskRef.child(user.uid).child(task.id).setValue(null)
            for (tagId: String in task.tagIds) {
                tagRef.child(tagId).child("tasks").child(task.id).setValue(null)
            }
        }
    }

    override fun exportCalendar(context: Context, taskID: String) {
        val calID: Long = 3
        var startMillis: Long = 0
        var endMillis: Long = 0
        val beginTime: Calendar = Calendar.getInstance()
        beginTime.set(2012, 9, 14, 7, 30)
        startMillis = beginTime.timeInMillis
        val endTime: Calendar = Calendar.getInstance()
        endTime.set(2012, 9, 14, 8, 45)
        endMillis = endTime.timeInMillis

        //val context:Con
        val cr: ContentResolver = context.contentResolver
        val values: ContentValues = ContentValues()
        values.put(CalendarContract.Events.DTSTART, startMillis)
        values.put(CalendarContract.Events.DTEND, endMillis)
        values.put(CalendarContract.Events.TITLE, "Jazzercise")
        values.put(CalendarContract.Events.DESCRIPTION, "Group workout")
        values.put(CalendarContract.Events.CALENDAR_ID, calID)
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles")
//        val uri: Uri? = if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED
//        ) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//
//            cr.insert(CalendarContract.Events.CONTENT_URI, values)
//
//
//
//        }
//        // get the event ID that is the last element in the Uri
//        val eventID:Long = Long.parseLong(uri?.lastPathSegment)
//
// ... do something with event ID
//
//

    }

    override fun cleanUp() {
        listenerTrackMap.forEach { (query, listener) ->
            query.removeEventListener(listener)
        }
        listTask.value = listOf()
        isListFetched = false
    }

    companion object {
        private const val TAG = "com.g2.taskstrackermvvm.model.repository.task"
    }
}