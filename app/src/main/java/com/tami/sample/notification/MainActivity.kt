package com.tami.sample.notification

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tami.notification.createNotification
import com.tami.notification.sendNotification

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        defaultNotification()
    }

    // DefaultNotification
    private fun defaultNotification() {
        val title = "Test"
        val message = "Message"
        val smallIcon = R.mipmap.ic_launcher

        val notification = this.createNotification(title = title, message = message, smallIcon = smallIcon)
        this.sendNotification(1, notification)
    }
}
