package com.udacity.utils

import android.graphics.Color
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.udacity.R

@BindingAdapter("app:showDownloadStatus")
fun TextView.changeStatus(downloadStatus:String?){
    if (downloadStatus == resources.getString(R.string.notification_success)){
        setText(R.string.notification_success)
        setTextColor(context.getColor(R.color.colorPrimary))
    }
    else{
        setText(R.string.notification_failure)
        setTextColor(Color.RED)
    }

}