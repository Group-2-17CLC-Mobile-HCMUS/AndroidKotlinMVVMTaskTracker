package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.model.repository.ITaskRepo

class UpdateTaskViewModel(
    private val taskRepo: ITaskRepo,
    private val tagRepo: ITaskRepo,
    private val subtaskRepo: ITaskRepo
) : ViewModel() {
    fun getTask(id: String): LiveData<Task> = taskRepo.getTaskDetail(id)
    fun updateTask(task: Task) = taskRepo.updateTask(task)
}