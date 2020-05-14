package com.g2.taskstrackermvvm.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Task constructor(val title: String,
                            val desc: String,
                            val priority: Priority,
                            val created: Date,
                            val dueDate: Date,
                            val status: Status = Status.Todo) {

    constructor() : this("", "", Priority.Low, Date(), Date())

//    private val mTagIds = mutableListOf<Int>()
    private val mSubTasks = mutableListOf<SubTask>()

    override fun toString(): String {
        return title + "\n" + desc + "\n" + priority.name + "\n" +
                created.toString() + "\n" + dueDate.toString() + "\n" + status.name
    }

    val subtasks : List<SubTask>
        get() = mSubTasks
//
//    fun changeTitle(newTitle: String) = Task(time, newTitle, detail)
//    fun changeDetail(newDetail: String) = Task(time, title, newDetail)
//
//    fun addTag(tagId: Int) = mTagIds.add(tagId)
//    fun removeTag(tagId: Int) = mTagIds.remove(tagId)

    enum class Status {
        Todo,
        Doing,
        Done
    }

    enum class Priority {
        Urgent,
        High,
        Medium,
        Low
    }

    class SubTask(val title: String) {
        var status: SubTaskStatus = SubTaskStatus.UNFINISHED
    }

    fun addSubTask(title: String) = mSubTasks.add(SubTask(title))

    enum class SubTaskStatus {
        UNFINISHED,
        FINISH
    }
}