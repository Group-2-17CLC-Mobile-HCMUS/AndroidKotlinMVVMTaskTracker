package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.model.repository.ITaskRepo

class HomeViewModel(val taskRepo: ITaskRepo) : ViewModel() {
    val tasks = taskRepo.getListTask()

    fun updateTask(task: Task) =
        taskRepo.updateTask(task)

}