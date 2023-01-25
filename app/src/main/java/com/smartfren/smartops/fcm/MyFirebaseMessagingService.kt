package com.smartfren.smartops.fcm

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.*
import android.graphics.Color
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.home.HomepageActivity
import com.smartfren.smartops.utils.BackgroundSoundService
import com.smartfren.smartops.utils.sendNotification
import java.util.*


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private  var count = 0

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
                scheduleJob()
            } else {
                // Handle message within 10 seconds
                handleNow()
            }

            MainApp.instance.sharedPreferences?.edit()
                ?.putString("titleFCM", remoteMessage.data["title"])
                ?.putString("bodyFCM", remoteMessage.data["body"])
                ?.putString("woIDFCM", remoteMessage.data["woid"])
                ?.putString("siteIDFCM", remoteMessage.data["siteid"])
                ?.putString("incwoIDFCM", remoteMessage.data["incwo_id"])
                ?.putString("statusFCM", remoteMessage.data["status"])
                ?.putString("raisedFCM", remoteMessage.data["raised"])
                ?.putString("clearedFCM", remoteMessage.data["cleared"])
                ?.putString("locationFCM", remoteMessage.data["location"])
                ?.putString("contentFCM", remoteMessage.data["content"])
                ?.putString("ne_idFCM", remoteMessage.data["ne_id"])
                ?.putString("typeFCM", remoteMessage.data["type"])
                ?.apply()

            if (remoteMessage.data["type"] == "2") {
//                sendNotification2(this, remoteMessage)
                sendNotification(remoteMessage)
            } else if (remoteMessage.data["type"] == "1" || remoteMessage.data["type"] == "3" ||
                remoteMessage.data["type"] == "4" || !remoteMessage.data["status"].isNullOrEmpty()) {
                Log.e("fcmCount", "$count")
                MainApp.instance.sharedPreferences?.edit()
                    ?.putString("notifFCMBackground", "" + count)
                    ?.apply()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(Intent(this, BackgroundSoundService::class.java))
                } else {
                    startService(Intent(this, BackgroundSoundService::class.java))
                }

                count++
            }
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    // [END on_new_token]

    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private fun sendNotification2(context: Context, remoteMessage: RemoteMessage) {
        val channelId = "fcm_smartops"
        val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()

        val sound =
            Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + context.packageName + "/" + R.raw.sound_alert) //Here is FILE_NAME is the name of file that you want to play

        // Create an explicit intent for an Activity in your app
        val intent = Intent(applicationContext, HomepageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val extras = Bundle()
        extras.putString("wo", remoteMessage.data["title"])
        extras.putString("woID", remoteMessage.data["woid"])
        extras.putString("incwoID", remoteMessage.data["incwo_id"])
        intent.putExtras(extras)
        intent.action = Intent.ACTION_VIEW

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val pendingIntent: PendingIntent = PendingIntent.getActivity(applicationContext, m, intent, flag)

        val notificationBuilder = NotificationCompat.Builder(context.applicationContext, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_smartops)
            .setContentTitle(remoteMessage.data["title"])
            .setContentText(remoteMessage.data["woid"])
            .setAutoCancel(true)
            .setLights(Color.BLUE, 500, 500)
            .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            .setSound(null)
            .setDefaults(Notification.DEFAULT_ALL)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setContentIntent(pendingIntent)

        val notificationManager = NotificationManagerCompat.from(context)

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val notificationChannel = NotificationChannel(channelId, "SmartOps", importance)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build()

            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.setSound(null, null)
            notificationChannel.enableVibration(true)
            notificationChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(notificationChannel)
        }

        notificationManager.notify(m, notificationBuilder.build())

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForegroundService(Intent(this, BackgroundSoundService::class.java))
//        } else {
//            startService(Intent(this, BackgroundSoundService::class.java))
//        }
//        val notification: Notification = notificationBuilder.build()
//        notification.flags = notification.flags or Notification.FLAG_INSISTENT

//        startService(Intent(this, BackgroundSoundService::class.java))

    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        val notificationManager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java) as NotificationManager
        notificationManager.sendNotification(remoteMessage, applicationContext)
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}