package com.smartfren.smartops.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.home.HomepageActivity
import com.smartfren.smartops.utils.Stopwatch.State
import com.smartfren.smartops.utils.Stopwatch.UpdateListener
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class StopwatchService : Service() {

    private val bus = EventBus.getDefault()
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var isStopping = false

    override fun onCreate() {
        super.onCreate()
        bus.register(this)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationBuilder = getServiceNotificationBuilder(
            getString(R.string.app_name),
            "Timess"
        )
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        isStopping = false
        startForeground(
            10001,
            notificationBuilder.build()
        )
        Stopwatch.addUpdateListener(updateListener)
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        bus.unregister(this)
        Stopwatch.removeUpdateListener(updateListener)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: StopwatchStopService) {
        isStopping = true
        stopForeground(true)
    }

    private fun getServiceNotificationBuilder(
        title: String,
        contentText: String
    ): NotificationCompat.Builder {
        val channelId = "simple_alarm_stopwatch"
        val label = "Check In time."
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, label, importance).apply {
                setSound(null, null)
                notificationManager.createNotificationChannel(this)
            }
        }

        val contentIntent = Intent(applicationContext, HomepageActivity::class.java)
        contentIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

        // Step 1.12 create PendingIntent
        val contentPendingIntent = PendingIntent.getActivity(
            applicationContext,
            9993,
            contentIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_timer)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(null)
            .setOngoing(true)
            .setAutoCancel(true)
            .setContentIntent(contentPendingIntent)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
    }

    private fun updateNotification(totalTime: Long) {
        val formattedDuration = totalTime.getFormattedDuration()
        notificationBuilder.setContentTitle(formattedDuration)
            .setContentText("Check In time.")
        notificationManager.notify(
            10001,
            notificationBuilder.build()
        )
    }

    private val updateListener = object : UpdateListener {
        override fun onUpdate(totalTime: Long, useLongerMSFormat: Boolean, schedule: Boolean) {
            if (!isStopping) {
                updateNotification(totalTime)
            }
        }

        override fun onStateChanged(state: State) {
            if (state == State.STOPPED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stopForeground(STOP_FOREGROUND_REMOVE)
                }
            }
        }
    }
}

fun startStopwatchService(context: Context) {
    Handler(Looper.getMainLooper()).post {
        ContextCompat.startForegroundService(context, Intent(context, StopwatchService::class.java))
    }
}

object StopwatchStopService
