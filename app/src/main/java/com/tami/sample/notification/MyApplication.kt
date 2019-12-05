package com.tami.sample.notification

import android.app.Application
import com.tami.notification.createNotificationChannel

class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
}