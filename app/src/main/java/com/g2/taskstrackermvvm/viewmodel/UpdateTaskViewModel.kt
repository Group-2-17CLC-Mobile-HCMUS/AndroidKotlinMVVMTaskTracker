package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
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

    fun updateTask(task: Task?) {
        task?.let {
            taskRepo.updateTask(task)
        }
    }

    fun rmSubtask(taskId: String, subtaskId: String) = subtaskRepo.removeSubTask(taskId, subtaskId)
}