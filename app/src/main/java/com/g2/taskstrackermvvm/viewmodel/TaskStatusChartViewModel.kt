package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.model.repository.ITaskRepo

class TaskStatusChartViewModel(private val taskRepo: ITaskRepo) : ViewModel() {
    val tasks = taskRepo.getListTask()
}