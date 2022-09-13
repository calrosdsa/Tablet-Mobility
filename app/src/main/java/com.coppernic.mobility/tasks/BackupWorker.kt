package com.coppernic.mobility.tasks

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coppernic.mobility.data.AppDatabase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted context:Context,
    @Assisted workerParameters: WorkerParameters,
    private val appDatabase: AppDatabase
): CoroutineWorker(context,workerParameters){

    companion object{
        const val TAG = "backup_worker_P"
        const val PASS = "12ab34cd56ef"
    }

    override suspend fun doWork(): Result {
//        val backUp = RoomBackup(applicationContext)
//        try{
//
//        backUp
//            .database(appDatabase)
//            .enableLogDebug(true)
//            .backupIsEncrypted(true)
//            .customEncryptPassword(PASS)
//            .backupLocation(RoomBackup.BACKUP_FILE_LOCATION_CUSTOM_FILE)
//            .maxFileCount(5)
//            .apply {
//                onCompleteListener { success, message, exitCode ->
//                    Log.d(TAG, "success: $success, message: $message, exitCode: $exitCode")
//                    if (success) restartApp(Intent(applicationContext, MainActivity::class.java))
//                }
//            }
//            .backup()
//        }catch(e:Throwable){
//            Log.d(TAG,e.localizedMessage?: "Error inesperado")
//            Result.failure()
//        }
        return Result.success()
    }

}