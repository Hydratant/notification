package com.tami.notification

import android.annotation.TargetApi
import android.app.*
import android.content.Context
import android.graphics.Bitmap
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat

const val NOTIFICATION_CHANNEL_ID = "DEFAULT"
const val NOTIFICATION_CHANNEL_NAME = "기본"

/**
 *  Notification Channel을 생성한다.
 *
 *  @param channelId : Notification ChannelId를 설정한다  ( Default : Default )
 *  @param channelName : Notification ChannelName을 설정한다 ( Default : 기본 )
 *  @param importance : Notification의 중요도를 설정한다 ( NONE, MIN, LOW, DEFAULT, HIGH, MAX / Default : DEFAULT)
 *
 */
@TargetApi(Build.VERSION_CODES.O)
fun Application.createNotificationChannel(
        channelId: String = NOTIFICATION_CHANNEL_ID,
        channelName: CharSequence = NOTIFICATION_CHANNEL_NAME,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        groupId: String? = null,
        description: String? = null,
        lights: Boolean? = null,
        lightColor: Int? = null,
        vibration: Boolean? = null,
        vibrationPattern: LongArray? = null,
        isShowBadge: Boolean = true,
        screenVisibility: Int = NotificationCompat.VISIBILITY_PUBLIC) {

    // Android O 이상 가능
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val context = applicationContext
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
        if (groupId != null) group = groupId                                // Channel Group 설정
        if (description != null) setDescription(description)                // 채널 설명 최대길이 300자
        if (lights != null) enableLights(lights)                            // 알림이 왔을 때 Light기능이 있는 장치에서 Light를 보여줄것인지 정한다.
        if (lightColor != null) setLightColor(lightColor)                   // 알림이 왔을 때 Light 기능이 true로 되어 있을 시 Light의 색깔을 정한다. ex) Color.Green
        if (vibration != null) enableVibration(vibration)                   // 알림이 왔을 때 진동여부를 정한다.
        if (vibrationPattern != null) setVibrationPattern(vibrationPattern) // 알림이 왔을 때 진동 패턴을 정한다. ex) val vibration = longArrayOf(100,200,100,200)
        lockscreenVisibility = screenVisibility                             // 알림이 왔을 때 잠금화면에서 보여줄 여부를 정한다. Default : Public
        setShowBadge(isShowBadge)                                           // 알림이 왔을 때 Badge Show 여부 설정
    }
    notificationManager.createNotificationChannel(notificationChannel)
}

@TargetApi(Build.VERSION_CODES.O)
fun Context.isNotificationChannels(): Boolean {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false

    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelLists = notificationManger.notificationChannels

    if (channelLists.isNullOrEmpty()) return false

    return true
}

@TargetApi(Build.VERSION_CODES.O)
fun Context.isNotificationChannel(channelId: String): Boolean {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false

    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelLists = notificationManger.notificationChannels

    if (channelLists.isNullOrEmpty()) return false

    for (channel in channelLists) {
        if (channel.id == channelId) return true
    }
    return true
}

@TargetApi(Build.VERSION_CODES.O)
fun Context.deleteNotificationChannel(channelId: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManger.deleteNotificationChannel(channelId)
}

@TargetApi(Build.VERSION_CODES.O)
fun Context.createNotificationGroup(id: String, name: CharSequence) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationChannelGroup = NotificationChannelGroup(id, name)
    notificationManger.createNotificationChannelGroup(notificationChannelGroup)
}

fun Context.createNotification(channelId: String = NOTIFICATION_CHANNEL_ID,
                               title: CharSequence,
                               message: CharSequence,
                               priority: Int = NotificationCompat.PRIORITY_DEFAULT,
                               autoCancel: Boolean = true,
                               sound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                               default: Int = NotificationCompat.DEFAULT_ALL,
                               badgeIcon: Int = NotificationCompat.BADGE_ICON_NONE,
                               smallIcon: Int? = null,
                               largeIcon: Bitmap? = null,
                               number: Int? = null,
                               contentIntent: PendingIntent? = null): Notification {

    return NotificationCompat.Builder(this, channelId).apply {
        setContentTitle(title)                                      // 알림의 제목
        setContentText(message)                                     // 알림의 내용
        setPriority(priority)                                       // 알림의 중요도 Oreo이상에서는 notificationChannel에 importance 가 우선 적용된다. ( Default NotificationCompat.PRIORITY_DEFAULT )
        setAutoCancel(autoCancel)                                   // 알림 클릭시 자동으로 없앨지 말지 정한다 ( Default true )
        setSound(sound)                                             // 알림이 왔을 때 소리
        setDefaults(default)                                        // 알림이 왔을 때 소리, 진동, 불빛 ( Default NotificationCompat.DEFAULT_ALL )
        setBadgeIconType(badgeIcon)                                 // Badge Icon Type ( Default NotificationCompat.BADGE_ICON_NONE )
        if (smallIcon != null) setSmallIcon(smallIcon)              // 알림의 제목 옆 작은 아이콘
        if (largeIcon != null) setLargeIcon(largeIcon)              // 알림의 큰 아이콘
        if (number != null) setNumber(number)                       // 읽지 않은 알림 갯수를 설정한다.
        if (contentIntent != null) setContentIntent(contentIntent)  // 알림을 클릭했을때 보내는 Intent 설정
    }.build()
}

fun Context.sendNotification(notificationId: Int, notification: Notification) {
    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManger.notify(notificationId, notification)
}

@TargetApi(Build.VERSION_CODES.O)
fun Application.initDefaultNotificationChannel(
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val isNotNotificationChannel = !isNotificationChannels()
    if (isNotNotificationChannel) {
        createNotificationChannel(importance = importance)
    }
}