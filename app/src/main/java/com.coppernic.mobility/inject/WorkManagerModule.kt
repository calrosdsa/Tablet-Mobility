package com.coppernic.mobility.inject

import android.content.Context
import androidx.work.WorkManager
import com.coppernic.mobility.util.impl.AppTasksImpl
import com.coppernic.mobility.util.interfaces.AppTasks
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object TasksModule {
    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)
}

@InstallIn(SingletonComponent::class)
@Module
abstract class TasksModuleBinds {
//    @Binds
//    @IntoSet
//    abstract fun provideAppTasksInitializer(bind: AppTasksInitializer): AppInitializer

    @Binds
    @Singleton
    abstract fun provideAppActions(bind: AppTasksImpl): AppTasks
}










//
//@Module
//@InstallIn(SingletonComponent::class)
//object WorkManagerInitializer : Initializer<WorkManager> {
//
//    @Provides
//    @Singleton
//    override fun create(@ApplicationContext context: Context): WorkManager {
//        val configuration = Configuration.Builder().build()
//        WorkManager.initialize(context, configuration)
//        Log.d("Hilt Init", "WorkManager initialized by Hilt this time")
//        return WorkManager.getInstance(context)
//    }
//
//    override fun dependencies(): List<Class<out Initializer<*>>> {
//        // No dependencies on other libraries.
//        return emptyList()
//    }
//
//    @Singleton
//    @Provides
//    fun provideWorkManagerConfiguration(
//        ioschedWorkerFactory: IoschedWorkerFactory
//    ): Configuration {
//        return Configuration.Builder()
//            .setMinimumLoggingLevel(Log.DEBUG)
//            .setWorkerFactory(ioschedWorkerFactory)
//            .build()
//    }
//
//    //Released Build
//
////    @Singleton
////    @Provides
////    fun provideWorkManagerConfiguration(
////        ioschedWorkerFactory: IoschedWorkerFactory
////    ): Configuration {
////        return Configuration.Builder()
////            .setWorkerFactory(ioschedWorkerFactory)
////            .build()
////    }
//}