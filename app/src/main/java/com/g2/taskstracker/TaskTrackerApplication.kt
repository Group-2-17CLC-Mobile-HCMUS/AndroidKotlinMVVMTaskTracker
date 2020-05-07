package com.g2.taskstracker

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class TaskTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@TaskTrackerApplication)
            modules(appModule)
        }

    }
}