package com.g2.taskstrackermvvm

import com.g2.taskstrackermvvm.model.repository.*
import com.g2.taskstrackermvvm.viewmodel.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val appModule = module {
    single<IUserRepo> { UserRepositoryImp() }
    single<ITaskRepo> { TaskRepositoryImp() }
    single<ITagRepo> { TagRepositoryImp() }
    single<ISubTaskRepo> { SubTaskRepositoryImp() }
    viewModel { TestViewModel(get(), get(), get()) }
//    viewModel { HomeListViewModel(get()) }
    viewModel { SignInViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { CreateTaskViewModel(get(), get()) }
    viewModel { TaskStatusChartViewModel(get()) }
    viewModel { TagsViewModel(get()) }
    viewModel { UpdateTaskViewModel(get(), get(), get()) }
}