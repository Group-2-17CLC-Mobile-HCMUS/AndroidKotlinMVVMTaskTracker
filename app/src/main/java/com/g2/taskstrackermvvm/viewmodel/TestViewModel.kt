package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.repository.ITagRepo
import com.g2.taskstrackermvvm.model.Task
import com.g2.taskstrackermvvm.model.repository.ITaskRepo
import com.g2.taskstrackermvvm.model.repository.IUserRepo
import java.util.*
import kotlin.random.Random

class TestViewModel(
                    private val userRepo: IUserRepo,
                    private val taskRepo: ITaskRepo,
                    private val tagRepo: ITagRepo) : ViewModel() {

    fun addUserTest() {
        userRepo.addUser()
    }

    fun updateUserTest() {
        val testPts = (0..100).random()
        userRepo.updateUser(testPts)
    }

    fun testAddTag() {
        tagRepo.createTag("Test", Tag.Color.RED)
    }

    fun setTag(taskId: String, tagId: String) {
        taskRepo.setTag(taskId, tagId)
    }

    fun addTaskTest() {
        taskRepo.addTask(title = "title02", desc = "desc02",
            priority = Task.Priority.Low,
            created = Date(2020, 3, 12),
            dueDate = Date(2020,3,5))
    }

    val listTaskData: LiveData<List<Task>> = taskRepo.getListTask()

    val listTagData: LiveData<List<Tag>> = tagRepo.getTagsList()
}


