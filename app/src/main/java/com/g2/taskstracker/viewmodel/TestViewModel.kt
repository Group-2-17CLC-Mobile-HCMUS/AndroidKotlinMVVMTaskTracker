package com.g2.taskstracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstracker.model.repository.TestRepositoryImp

class TestViewModel(repo: TestRepositoryImp) : ViewModel() {
    val dataText: LiveData<String> = repo.basicReadWrite()


}
