package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.model.repository.ITaskRepo
import java.util.*

class CreateTaskViewModel(private val taskRepo: ITaskRepo) : ViewModel() {
    fun addTask(title: String, desc: String) {
        taskRepo.addTask(title, desc, Task.Priority.Low, Date(), Date())
    }
}