package com.udacity.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.udacity.ButtonState
import com.udacity.LoadingButton
import com.udacity.R
import com.udacity.utils.NotificationHelpers.showNotification

private const val UDACITY_URL =
    "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
private const val GLIDE_URL =
    "https://github.com/bumptech/glide/archive/master.zip"
private const val RETROFIT_URL =
    "https://github.com/square/retrofit/archive/master.zip"

object DownloadHelpers {
    private var downloadID: Long = 0

    var downloadStatus = ""

    var downloadTitle = ""

    fun DownloadManager.determineDownloadStatus(id: Long, context: Context) {
        val query = DownloadManager.Query()
        query.setFilterById(id)
        val cursor = query(query)
        if (cursor.moveToFirst()) {
            val currentDownloadStatus = cursor.getInt(
                cursor.getColumnIndex(
                    DownloadManager.COLUMN_STATUS
                )
            )

            downloadStatus = context.getString(
                when (currentDownloadStatus) {
                    DownloadManager.STATUS_SUCCESSFUL -> R.string.notification_success
                    else -> R.string.notification_failure
                }
            )


        }

    }


    fun AppCompatActivity.download(lb: LoadingButton, rg: RadioGroup) {
        val downloadManager =
            getSystemService(AppCompatActivity.DOWNLOAD_SERVICE) as DownloadManager
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                downloadManager.determineDownloadStatus(id as Long, this@download)
                if (id.toInt() != -1) {
                    lb.buttonState = ButtonState.Completed
                    showNotification(this@download)


                }
            }
        }
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        val radioButtonID = rg.checkedRadioButtonId
        if (radioButtonID != -1) {
            lb.buttonState = ButtonState.Loading
            val request = DownloadManager.Request(
                Uri.parse(
                    when (radioButtonID) {
                        R.id.glide_radio -> GLIDE_URL
                        R.id.udacity_radio -> UDACITY_URL
                        R.id.retrofit_radio -> RETROFIT_URL
                        else -> return
                    }
                )
            )
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)



            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.
            downloadTitle = when(radioButtonID ){
                R.id.glide_radio -> resources.getString(R.string.glide_radio_label)
                R.id.udacity_radio -> resources.getString(R.string.udacity_radio_label)
                R.id.retrofit_radio -> resources.getString(R.string.retrofit_radio_label)
                else -> resources.getString(R.string.unknown_file_name)
            }

        } else {
            Toast.makeText(
                this,
                R.string.no_button_selected_warning, Toast.LENGTH_SHORT
            ).show()
        }
    }
}






