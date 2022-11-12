package com.coppernic.mobility.util.impl

import android.app.Application
import com.coppernic.mobility.util.interfaces.TaskAppInitializer
import com.coppernic.mobility.util.interfaces.AppTasks
import javax.inject.Inject
import dagger.Lazy
import kotlinx.coroutines.delay

class TaskAppTasksInitializer @Inject constructor(
    private val appTasks: Lazy<AppTasks>
) :TaskAppInitializer {
    override fun init(application: Application) {
//        appTasks.get().sendMarcaciones()
        appTasks.get().getDataServer()
    }
}