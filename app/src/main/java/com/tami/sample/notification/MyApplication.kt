package com.tami.sample.notification

import android.app.Application
import com.tami.notification.initDefaultNotificationChannel

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // 간편하게 Default Channle을 생성하고 싶을 때 사용
        initDefaultNotificationChannel()
    }
}