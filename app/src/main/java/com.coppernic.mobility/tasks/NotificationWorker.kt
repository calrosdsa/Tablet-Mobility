package com.coppernic.mobility.tasks

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.coppernic.mobility.MainActivity
import com.coppernic.mobility.R
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted context:Context,
    @Assisted workerParameters: WorkerParameters
):
   CoroutineWorker(context,workerParameters){



    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        Log.d("WORKER_ACCESS","INICIOOO")
        showNotification()
        return Result.success()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showNotification(){
        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0, intent, 0
        )
        val notification = NotificationCompat.Builder(
            applicationContext,
            CHANNEL_ID
        )
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("New Task")
            .setContentText("Subscribe on the channel")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val channelName = "Channel Name"
        val channelDescription = "Channel Description"
        val channelImportance = NotificationManager.IMPORTANCE_HIGH

        val channel = NotificationChannel(CHANNEL_ID, channelName, channelImportance).apply {
            description = channelDescription
        }

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.createNotificationChannel(channel)


        with(NotificationManagerCompat.from(applicationContext)) {
            notify(NOTIFICATION_ID, notification.build())
        }

    }
companion object {
    const val TAG = "notification-worker"
    const val CHANNEL_ID = "channel_id"
    const val NOTIFICATION_ID = 1
}
    }


