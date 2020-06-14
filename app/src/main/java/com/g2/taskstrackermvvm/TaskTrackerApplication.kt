package com.g2.taskstrackermvvm

import android.app.Application
import com.yariksoffice.lingver.Lingver
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class TaskTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Lingver.init(this, "en")
        startKoin {
            androidContext(this@TaskTrackerApplication)
            modules(appModule)
        }

    }
}