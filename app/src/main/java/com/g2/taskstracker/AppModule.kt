package com.g2.taskstracker

import com.g2.taskstracker.model.repository.TestRepositoryImp
import com.g2.taskstracker.viewmodel.TestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

@JvmField
val appModule = module {
    single<TestRepositoryImp> { TestRepositoryImp() }
    viewModel { TestViewModel(get()) }
}