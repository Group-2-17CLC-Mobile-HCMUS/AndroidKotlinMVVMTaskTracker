package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.ViewModel

import com.g2.taskstrackermvvm.model.repository.ITaskRepo

class ListTaskViewModel(private val taskRepo: ITaskRepo) : ViewModel() {
    val data = taskRepo.getListTask();

}