package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.SubTask
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.model.repository.ISubTaskRepo
import com.g2.taskstrackermvvm.model.repository.ITagRepo
import com.g2.taskstrackermvvm.model.repository.ITaskRepo

class UpdateTaskViewModel(
    private val taskRepo: ITaskRepo,
    private val tagRepo: ITagRepo,
    private val subtaskRepo: ISubTaskRepo
) : ViewModel() {
    private val mTagList: LiveData<List<Tag>> = tagRepo.getTagsList()
    val tagList: LiveData<List<Tag>>
        get() = mTagList

    fun getTask(id: String): LiveData<Task> = taskRepo.getTaskDetail(id)
    fun getTag(id: String): Tag? = tagRepo.getTagById(id)
    fun setTags(taskId: String, tagIds: List<String>) {
        tagIds.forEach {
            taskRepo.setTag(taskId, it)
        }
    }

    fun removeTags(taskId: String, tagIds: List<String>) {
        tagIds.forEach {
            taskRepo.removeTag(taskId, it)
        }
    }

    fun updateTask(task: Task?) {
        task?.let {
            taskRepo.updateTask(task)
        }
    }

    fun addSubtask(name: String, taskId: String) = subtaskRepo.addSubTask(name, taskId)
    fun updateSubtask(taskId: String, subtask: SubTask) = subtaskRepo.updateTask(subtask, taskId)
    fun rmSubtask(taskId: String, subtaskId: String) = subtaskRepo.removeSubTask(taskId, subtaskId)
    fun cleanUp(taskId: String) {
        taskRepo.clearSingleTask(taskId)
        subtaskRepo.cleanUp()
    }
}