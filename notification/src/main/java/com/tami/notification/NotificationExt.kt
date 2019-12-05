@file:Suppress("SpellCheckingInspection")

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
 *  @param groupId : Notification Ground을 설정하는 ID
 *  @param description : 채널 설명 최대길이 300자
 *  @param isLights : 알림이 왔을 때 Light기능이 있는 장치에서 Light를 보여줄것인지 정한다.
 *  @param lightColor : 알림이 왔을 때 Light 기능이 true로 되어 있을 시 Light의 색깔을 정한다. ex) Color.Green
 *  @param vibration : 알림이 왔을 때 진동여부를 정한다.
 *  @param vibrationPattern : 알림이 왔을 때 진동 패턴을 정한다. ex) val vibration = longArrayOf(100,200,100,200)
 *  @param screenVisibility : 알림이 왔을 때 잠금화면에서 보여줄 여부를 정한다. Default : Public
 *  @param isShowBadge : 알림이 왔을 때 Badge Show 여부 설정
 *
 */
@TargetApi(Build.VERSION_CODES.O)
fun Application.createNotificationChannel(
        channelId: String = NOTIFICATION_CHANNEL_ID,
        channelName: CharSequence = NOTIFICATION_CHANNEL_NAME,
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT,
        groupId: String? = null,
        description: String? = null,
        isLights: Boolean? = null,
        lightColor: Int? = null,
        vibration: Boolean? = null,
        vibrationPattern: LongArray? = null,
        screenVisibility: Int = NotificationCompat.VISIBILITY_PUBLIC,
        isShowBadge: Boolean = true) {

    // Android O 이상 가능
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    // 해당 Channel이 생성되어있으면 생성 로직을 타지 않는다.
    val isCreateChannel = isNotificationChannel(channelId)
    if (isCreateChannel) return

    val context = applicationContext
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationChannel = NotificationChannel(channelId, channelName, importance).apply {
        if (groupId != null) group = groupId                                // Channel Group 설정
        if (description != null) setDescription(description)                // 채널 설명 최대길이 300자
        if (isLights != null) enableLights(isLights)                        // 알림이 왔을 때 Light기능이 있는 장치에서 Light를 보여줄것인지 정한다.
        if (lightColor != null) setLightColor(lightColor)                   // 알림이 왔을 때 Light 기능이 true로 되어 있을 시 Light의 색깔을 정한다. ex) Color.Green
        if (vibration != null) enableVibration(vibration)                   // 알림이 왔을 때 진동여부를 정한다.
        if (vibrationPattern != null) setVibrationPattern(vibrationPattern) // 알림이 왔을 때 진동 패턴을 정한다. ex) val vibration = longArrayOf(100,200,100,200)
        lockscreenVisibility = screenVisibility                             // 알림이 왔을 때 잠금화면에서 보여줄 여부를 정한다. Default : Public
        setShowBadge(isShowBadge)                                           // 알림이 왔을 때 Badge Show 여부 설정
    }
    notificationManager.createNotificationChannel(notificationChannel)
}

/**
 * NotificationChannel 생성 여부를 체크한다.
 *
 * @return true : 채널이 하나라도 생성되어있음 , false : 채널이 하나도 생성되어있지않음.
 */
@TargetApi(Build.VERSION_CODES.O)
fun Context.isNotificationChannels(): Boolean {

    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return false

    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val channelLists = notificationManger.notificationChannels

    if (channelLists.isNullOrEmpty()) return false

    return true
}

/**
 * 해당 NotificationChannel 생성 여부를 체크한다.
 *
 * @param channelId : 체크할 ChannelId
 * @return true : 해당채널이 생성되어있다 / false : 해당채널이 생성되어있지 않다.
 *
 */
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

/**
 * 해당 NotificationChannel을 삭제한다.
 *
 *  @param channelId : 삭제 할 ChannelId
 *
 */
@TargetApi(Build.VERSION_CODES.O)
fun Context.deleteNotificationChannel(channelId: String) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManger.deleteNotificationChannel(channelId)
}

/**
 *  Noticiation Group을 만든다.
 *
 *  @param id : 해당 Group id
 *  @param name : 해당 Group Name
 */
@TargetApi(Build.VERSION_CODES.O)
fun Context.createNotificationGroup(id: String, name: CharSequence) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val notificationChannelGroup = NotificationChannelGroup(id, name)
    notificationManger.createNotificationChannelGroup(notificationChannelGroup)
}

/**
 *
 * Notification을 생성한다.
 *
 * @param channelId : 생성할 Notification의 ChannelId를 정한다.
 * @param title : Notification의 Title
 * @param message : Notification의 Message
 * @param priority : Notification의 중요도를 정한다. ( Oreo 이상은 Channel priority를 따라간다. )
 * @param autoCancel : 알림 클릭 시 자동으로 Notification Cancel 여부를 정한다.
 * @param sound : 알림이 왔을 때 소리 설정
 * @param default : 알림이 왔을 때 소리, 진동, 불빛 설정 ( Default NotificationCompat.DEFAULT_ALL )
 * @param badgeIcon : Badge Icon Type ( Default NotificationCompat.BADGE_ICON_NONE )
 * @param smallIcon : Notification 의 Icon
 * @param largeIcon : Notification 의 LargeIcon
 * @param number : 읽지 않은 알림 갯수를 설정한다.
 * @param contentIntent : 알림을 클릭했을 때 Intent 설정
 *
 * @return 해당 옵션으로 생성 된 Notification
 *
 */
fun Context.createNotification(channelId: String = NOTIFICATION_CHANNEL_ID,
                               title: CharSequence,
                               message: CharSequence,
                               priority: Int = NotificationCompat.PRIORITY_DEFAULT,
                               autoCancel: Boolean = true,
                               sound: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION),
                               default: Int = NotificationCompat.DEFAULT_ALL,
                               badgeIcon: Int = NotificationCompat.BADGE_ICON_NONE,
                               smallIcon: Int,
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
        setSmallIcon(smallIcon)                                     // 알림의 제목 옆 작은 아이콘
        if (largeIcon != null) setLargeIcon(largeIcon)              // 알림의 큰 아이콘
        if (number != null) setNumber(number)                       // 읽지 않은 알림 갯수를 설정한다.
        if (contentIntent != null) setContentIntent(contentIntent)  // 알림을 클릭했을때 보내는 Intent 설정
    }.build()
}

/**
 *
 * Notification을 보낸다.
 *
 * @param notificationId : 해당 NotificationId
 * @param notification : 보낼 Notification
 *
 */
fun Context.sendNotification(notificationId: Int, notification: Notification) {
    val notificationManger = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManger.notify(notificationId, notification)
}

/**
 * DefaultNotificationChannel을 생성한다.
 */
@TargetApi(Build.VERSION_CODES.O)
fun Application.initDefaultNotificationChannel(
        importance: Int = NotificationManager.IMPORTANCE_DEFAULT) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

    val isNotNotificationChannel = !isNotificationChannels()
    if (isNotNotificationChannel) {
        createNotificationChannel(importance = importance)
    }
}