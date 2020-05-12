package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.Tag
import com.g2.taskstrackermvvm.model.repository.ITagRepo
import com.g2.taskstrackermvvm.model.repository.IUserRepo
import com.g2.taskstrackermvvm.model.repository.TestRepositoryImp
import java.util.*
import kotlin.random.Random

class TestViewModel(private val repo: TestRepositoryImp, private val userRepo: IUserRepo, private val tagsRepo: ITagRepo) : ViewModel() {
    val dataText: LiveData<String> = repo.basicReadWrite()

    fun addUserTest() {
        userRepo.addUser(0)
    }

    fun updateUserTest() {
        val testPts = (0..100).random()
        userRepo.updateUser(testPts)
    }

    fun testAddTag() {
        tagsRepo.createTag("Test", Tag.Color.RED)
    }
}


