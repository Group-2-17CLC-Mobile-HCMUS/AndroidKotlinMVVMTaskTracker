package com.g2.taskstracker.model

import java.util.*

data class Task constructor(val time: Date, val title: String, val detail: String, val id: Int = nextId++) {
    companion object {
        private var nextId = 0;
    }
    private val mTagIds = mutableListOf<Int>()
    private val mSubTasks = mutableListOf<SubTask>()

    val subtasks : List<SubTask>
        get() = mSubTasks

    fun changeTitle(newTitle: String) = Task(time, newTitle, detail, id)
    fun changeDetail(newDetail: String) = Task(time, title, newDetail, id)

    fun addTag(tagId: Int) = mTagIds.add(tagId)
    fun removeTag(tagId: Int) = mTagIds.remove(tagId)

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