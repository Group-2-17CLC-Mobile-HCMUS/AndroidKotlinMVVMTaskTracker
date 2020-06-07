package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.repository.ITagRepo
import com.g2.taskstrackermvvm.model.repository.ITaskRepo

class MainActivityViewModel(
    private val taskRepo: ITaskRepo,
    private val tagRepo: ITagRepo,
    private val subtaskRepo: ITagRepo
) :
    ViewModel() {
    fun cleanUp() {
        taskRepo.cleanUp()
        tagRepo.cleanUp()
        subtaskRepo.cleanUp()
    }
}