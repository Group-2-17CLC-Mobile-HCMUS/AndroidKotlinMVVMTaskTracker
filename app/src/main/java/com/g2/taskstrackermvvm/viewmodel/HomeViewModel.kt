package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.model.repository.ITagRepo
import com.g2.taskstrackermvvm.model.repository.ITaskRepo

class HomeViewModel(
    private val taskRepo: ITaskRepo
    , private val tagRepo: ITagRepo
) : ViewModel() {
    val tasks = taskRepo.getListTask()
    val tags = tagRepo.getTagsList()

    fun updateTask(task: Task) =
        taskRepo.updateTask(task)

    fun removeTask(task: Task) = taskRepo.removeTask(task)

    fun getTagById(id: String): Tag? {
        return tagRepo.getTagById(id)
    }
}