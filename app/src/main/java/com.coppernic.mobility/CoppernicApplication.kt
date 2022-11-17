package com.coppernic.mobility

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.coppernic.mobility.util.interfaces.TaskAppInitializer
import com.jakewharton.threetenabp.AndroidThreeTen
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CoppernicApplication:Application(), Configuration.Provider{
    @Inject lateinit var initializers: TaskAppInitializer
    @Inject lateinit var workerFactory: HiltWorkerFactory


    override fun onCreate() {
        super.onCreate()
        initializers.init()
        AndroidThreeTen.init(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
//    @Inject lateinit var workerFactory: HiltWorkerFactory
//
////    override fun onCreate() {
////        super.onCreate()
////        initializers.init(this)
////    }
//
//    override fun getWorkManagerConfiguration(): androidx.work.Configuration {
//        return androidx.work.Configuration.Builder()
//            .setWorkerFactory(workerFactory)
//            .build()
//    }
}