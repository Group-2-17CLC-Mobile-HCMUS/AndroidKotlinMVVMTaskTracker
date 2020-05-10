package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.repository.ITestListRepo

class HomeListViewModel(repo: ITestListRepo) : ViewModel() {
    val data = repo.getTaskList()
}
