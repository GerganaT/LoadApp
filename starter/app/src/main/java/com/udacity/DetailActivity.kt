package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.udacity.databinding.ActivityDetailBinding
import com.udacity.utils.FILE_NAME
import com.udacity.utils.FILE_STATUS
import com.udacity.utils.NotificationHelpers.cancelNotification
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    lateinit var downloadStatus: String
    lateinit var downloadedFileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = setContentView(this, R.layout.activity_detail) as ActivityDetailBinding
        binding.detailActivity = this
        setSupportActionBar(toolbar)
        downloadStatus = intent.getStringExtra(FILE_STATUS).toString()
        downloadedFileName = intent.getStringExtra(FILE_NAME).toString()
        cancelNotification()

    }

}
