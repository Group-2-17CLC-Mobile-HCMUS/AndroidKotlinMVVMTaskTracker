package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.repository.TestRepositoryImp

class TestViewModel(repo: TestRepositoryImp) : ViewModel() {
    val dataText: LiveData<String> = repo.basicReadWrite()


}
