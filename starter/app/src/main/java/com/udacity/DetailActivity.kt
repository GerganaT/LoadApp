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
