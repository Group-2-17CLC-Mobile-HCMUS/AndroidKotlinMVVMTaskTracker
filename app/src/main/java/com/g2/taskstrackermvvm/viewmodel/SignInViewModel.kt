package com.g2.taskstrackermvvm.viewmodel

import androidx.lifecycle.ViewModel
import com.g2.taskstrackermvvm.model.repository.IUserRepo

class SignInViewModel(private val userRepo: IUserRepo) : ViewModel() {
    fun addCurrentUser() {
        userRepo.addUser()
    }
}