package com.udacity

import android.app.DownloadManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action
    private lateinit var radioGroup: RadioGroup


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        radioGroup = findViewById(R.id.radioGroup)
        custom_button.setOnClickListener {
            download()
        }
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id?.toInt() != -1) {
                custom_button.buttonState = ButtonState.Completed
                Toast.makeText(context, "file downloaded", Toast.LENGTH_SHORT).show()
                //TODO show file and remove toast ?
            }
        }
    }

    private fun download() {
        val radioButtonID = radioGroup.checkedRadioButtonId
        if (radioButtonID != -1) {
            custom_button.buttonState = ButtonState.Loading
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

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            downloadID =
                downloadManager.enqueue(request)// enqueue puts the download request in the queue.

        } else {
            Toast.makeText(
                this,
                R.string.no_button_selected_warning, Toast.LENGTH_SHORT
            ).show()
        }
    }

    //TODO why do we do a companion object here? simply add constants at the top of the class
    companion object {
        private const val UDACITY_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val GLIDE_URL =
            "https://github.com/bumptech/glide/archive/refs/heads/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/refs/heads/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
