package com.g2.taskstrackermvvm.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

@IgnoreExtraProperties
data class Task constructor(@get:Exclude var id: String,
                            val title: String,
                            val desc: String,
                            val priority: Priority,
                            val created: Date,
                            val dueDate: Date,
                            val status: Status = Status.Todo,
                            @get:Exclude val tagIds: MutableList<String> = mutableListOf(),
                            @get:Exclude val subTasks : MutableList<SubTask> = mutableListOf()
) {

    constructor() : this("","", "", Priority.Low, Date(), Date())

    override fun toString(): String {
        return "$id\n$title"
    }

    fun addTag(id: String) {
        tagIds.add(id)
    }

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


}