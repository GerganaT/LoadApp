/* Copyright 2021,  Gergana Kirilova

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package com.udacity.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.DetailActivity
import com.udacity.R
import com.udacity.utils.DownloadHelpers.downloadStatus
import com.udacity.utils.DownloadHelpers.downloadTitle

private const val NOTIFICATION_ID = 0

const val FILE_NAME = "com.udacity.FILE_NAME"
const val FILE_STATUS = "com.udacity.FILE_STATUS"


object NotificationHelpers {

    private lateinit var notificationManager: NotificationManager

    private fun NotificationManager.buildNotification(
        notificationMessage: String,
        applicationContext: Context
    ) {

        val intent = Intent(applicationContext, DetailActivity::class.java).apply {
            putExtra(FILE_STATUS, downloadStatus)
            putExtra(FILE_NAME, downloadTitle)
        }

        val pendingIntent = PendingIntent.getActivity(
            applicationContext, NOTIFICATION_ID,
            intent, FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(
            applicationContext, applicationContext.getString(R.string.channel_id)
        )
            .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
            .setContentTitle(notificationMessage)
            .setPriority(
                NotificationCompat.PRIORITY_LOW
            )

            .addAction(
                R.drawable.ic_baseline_cloud_download_24,
                applicationContext.getString(R.string.notification_button),
                pendingIntent
            )
        notify(
            NOTIFICATION_ID,
            notificationBuilder.build()
        )

    }

    fun showNotification(applicationContext: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                applicationContext.getString(R.string.channel_id),
                applicationContext.getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.description = "Download status"
            notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.buildNotification(
                applicationContext.getString(R.string.notification_title), applicationContext
            )
        }
    }

    fun cancelNotification() {
        notificationManager.cancel(NOTIFICATION_ID)
    }
}
