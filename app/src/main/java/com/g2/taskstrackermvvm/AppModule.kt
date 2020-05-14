package com.g2.taskstrackermvvm

import com.g2.taskstrackermvvm.model.repository.*
import com.g2.taskstrackermvvm.viewmodel.HomeListViewModel
import com.g2.taskstrackermvvm.viewmodel.SignInViewModel
import com.g2.taskstrackermvvm.viewmodel.TestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val appModule = module {
    single<TestRepositoryImp> { TestRepositoryImp() }
    single<IUserRepo> { UserRepositoryImp() }
    single<ITestListRepo> { TestListRepoImp() }
    single<ITaskRepo> { TaskRepositoryImp() }
    viewModel { TestViewModel(get(), get(), get()) }
    viewModel { HomeListViewModel(get()) }
    viewModel { SignInViewModel(get()) }
}