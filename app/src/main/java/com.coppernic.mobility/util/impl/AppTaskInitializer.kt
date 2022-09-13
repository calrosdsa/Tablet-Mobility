package com.coppernic.mobility.util.impl

import android.app.Application
import com.coppernic.mobility.util.interfaces.AppInitializer
import com.coppernic.mobility.util.interfaces.AppTasks
import javax.inject.Inject
import dagger.Lazy

class AppTasksInitializer @Inject constructor(
    private val appTasks: Lazy<AppTasks>
) :AppInitializer {
    override fun init(application: Application) {
        appTasks.get().deleteMarcaciones()
    }
}