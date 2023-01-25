/*
 * Copyright (C) 2019 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.smartfren.smartops.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.home.HomepageActivity
import com.smartfren.smartops.ui.workordermanagement.WorkorderActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

// TODO: Step 1.1 extension function to send messages (GIVEN)
/**
 * Builds and delivers the notification.
 *
 * @param context, activity context.
 */
@ExperimentalCoroutinesApi
fun NotificationManager.sendNotification(remoteMessage: RemoteMessage, applicationContext: Context) {
    // Create the content intent for the notification, which launches
    // this activity
    // TODO: Step 1.11 create intent
//    val contentIntent = Intent(applicationContext, WorkorderActivity::class.java)
    // TODO: Step 1.12 create PendingIntent
//    val contentPendingIntent = PendingIntent.getActivity(
//        applicationContext,
//        NOTIFICATION_ID,
//        contentIntent,
//        PendingIntent.FLAG_UPDATE_CURRENT
//    )
    val channelId = "fcm_smartops"
    val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()
    val contentIntent = Intent(applicationContext, HomepageActivity::class.java)
    val extras = Bundle()

    extras.putString("wo", remoteMessage.data["title"])
    extras.putString("woID", remoteMessage.data["woid"])
    extras.putString("incwoID", remoteMessage.data["incwo_id"])
    contentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    contentIntent.putExtras(extras)
    contentIntent.action = Intent.ACTION_VIEW

    val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.FLAG_UPDATE_CURRENT
    } else {
        PendingIntent.FLAG_UPDATE_CURRENT
    }

    val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, m, contentIntent, flag)

    // TODO: Step 2.0 add style
    val logoImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.mipmap.ic_launcher_smartops
    )
    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(logoImage)
        .bigLargeIcon(null)

    val sound =
        Uri.parse("android.resource://" + applicationContext.packageName + "/" + R.raw.sound_alert) //Here is FILE_NAME is the name of file that you want to play

    // TODO: Step 2.2 add snooze action
//    val snoozeIntent = Intent(applicationContext, SnoozeReceiver::class.java)
//    val snoozePendingIntent: PendingIntent = PendingIntent.getBroadcast(
//        applicationContext,
//        REQUEST_CODE,
//        snoozeIntent,
//        FLAGS
//    )

    // TODO: Step 1.2 get an instance of NotificationCompat.Builder
    // Build the notification
    val builder = NotificationCompat.Builder(applicationContext, channelId)

        // TODO: Step 1.8 use the new 'breakfast' notification channel

        // TODO: Step 1.3 set title, text and icon to builder
        .setSmallIcon(R.mipmap.ic_launcher_smartops)
        .setContentTitle(remoteMessage.data["title"])
        .setContentText(remoteMessage.data["woid"])

        // TODO: Step 1.13 set content intent
        .setContentIntent(pendingIntent)
        .setAutoCancel(true)

        // TODO: Step 2.1 add style to builder
//        .setStyle(bigPicStyle)
//        .setLargeIcon(eggImage)
        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
        .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
//        .setSound(sound)

        // TODO: Step 2.3 add snooze action
//        .addAction(
//            R.drawable.egg_icon,
//            applicationContext.getString(R.string.snooze),
//            snoozePendingIntent
//        )

        // TODO: Step 2.5 set priority
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val notificationChannel = NotificationChannel(channelId, "SmartOps", importance)

        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.setSound(null, null)
        notificationChannel.enableVibration(true)
        notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
        this.createNotificationChannel(notificationChannel)
    }

    // TODO: Step 1.4 call notify
    notify(m, builder.build())
}

// TODO: Step 1.14 Cancel all notifications
/**
 * Cancels all notifications.
 *
 */
fun NotificationManager.cancelNotifications() {
    cancelAll()
}
