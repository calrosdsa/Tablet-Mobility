package com.coppernic.mobility.util.impl

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.coppernic.mobility.tasks.*
import com.coppernic.mobility.util.interfaces.AppTasks
import javax.inject.Inject

class AppTasksImpl @Inject constructor(
    private val workManager: WorkManager
) : AppTasks {

    override fun mainTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .build()
        val request2 = OneTimeWorkRequestBuilder<DeleteMarcacionesWorker>()
            .addTag(DeleteMarcacionesWorker.TAG)
            .build()
        val request1 = OneTimeWorkRequestBuilder<GetDataServer>()
            .setConstraints(constraints)
            .addTag(GetDataServer.TAG)
            .build()
        workManager.beginWith(request1)
            .then(request2)
            .enqueue()
    }
    override fun deleteMarcaciones() {
        val request2 = OneTimeWorkRequestBuilder<DeleteMarcacionesWorker>()
            .addTag(DeleteMarcacionesWorker.TAG)
            .build()
        workManager.beginWith(request2).enqueue()
    }

    override fun showNotifications() {
        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            .addTag(NotificationWorker.TAG)
            .build()
        workManager.enqueue(request)
    }

    override fun getDataServer() {
        val request = OneTimeWorkRequestBuilder<GetDataServer>()
//            .setInitialDelay(5,TimeUnit.SECONDS)
            .addTag(GetDataServer.TAG)
            .build()
        workManager.enqueue(request)
    }

    override fun backUpRoom() {
        val request = OneTimeWorkRequestBuilder<BackupWorker>()
//            .setInitialDelay(5,TimeUnit.SECONDS)
            .addTag(BackupWorker.TAG)
            .build()
        workManager.enqueue(request)
    }

    override fun sendMarcaciones() {
        val request = OneTimeWorkRequestBuilder<SendMarcacionesWorker>()
            .addTag(SendMarcacionesWorker.TAG)
            .build()
        workManager.enqueue(request)
    }
    override fun sendMarcacion() {
        val request = OneTimeWorkRequestBuilder<SendMarcacionesWorker>()
            .addTag(SendMarcacionWorker.TAG)
            .build()
        workManager.enqueue(request)
    }


}