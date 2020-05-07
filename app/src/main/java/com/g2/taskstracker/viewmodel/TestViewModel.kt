package com.g2.taskstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstracker.model.repository.TestRepository

class TestViewModel : ViewModel() {
    private val repo = TestRepository()
    val dataText: LiveData<String> = repo.basicReadWrite()


}
