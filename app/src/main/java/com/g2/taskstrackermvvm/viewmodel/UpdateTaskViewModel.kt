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
    fun getTask(id: String): LiveData<Task> = taskRepo.getTaskDetail(id)
    fun getTag(id: String): Tag? = tagRepo.getTagById(id)
    fun updateTask(task: Task) = taskRepo.updateTask(task)
    fun rmSubtask(taskId: String, subtaskId: String) = subtaskRepo.removeSubTask(taskId, subtaskId)
}