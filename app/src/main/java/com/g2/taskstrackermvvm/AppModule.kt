package com.g2.taskstrackermvvm

import com.g2.taskstrackermvvm.model.repository.IUserRepo
import com.g2.taskstrackermvvm.model.repository.TestRepositoryImp
import com.g2.taskstrackermvvm.model.repository.UserRepositoryImp
import com.g2.taskstrackermvvm.viewmodel.TestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val appModule = module {
    single<TestRepositoryImp> { TestRepositoryImp() }
    single<IUserRepo> { UserRepositoryImp() }
    viewModel { TestViewModel(get(), get()) }
}