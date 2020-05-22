package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.repository.ITaskRepo

class HomeViewModel(taskRepo: ITaskRepo) : ViewModel() {
    val tasks = taskRepo.getListTask()
}