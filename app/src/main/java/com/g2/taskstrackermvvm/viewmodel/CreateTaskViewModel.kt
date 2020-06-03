package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.model.repository.ITagRepo
import com.g2.taskstrackermvvm.model.repository.ITaskRepo
import java.util.*

class CreateTaskViewModel(
    private val taskRepo: ITaskRepo,
    private val tagRepo: ITagRepo
) :
    ViewModel() {

    private val mTagList: LiveData<List<Tag>> = tagRepo.getTagsList()
    val tagList: LiveData<List<Tag>>
        get() = mTagList


    fun addTask(title: String, desc: String) {
        taskRepo.addTask(title, desc, Task.Priority.Low, Date(), Date())
    }
}