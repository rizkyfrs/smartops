package com.smartfren.smartops.utils

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.R
import com.smartfren.smartops.ui.home.HomepageActivity
import kotlinx.coroutines.*
import java.util.*


class BackgroundSoundService : Service() {
    var player: MediaPlayer? = null
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder
    var mAudioManager: AudioManager? = null
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.Main + serviceJob)

    override fun onBind(arg0: Intent): IBinder? {
        Log.i(TAG, "onBind()")
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "onCreate()")
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        player = MediaPlayer()
        player = MediaPlayer.create(this, R.raw.sound_alert)
        player?.isLooping = true // Set looping
        mAudioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        mAudioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
        player?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        player?.setVolume(0.09f, 0.09f)
        player?.setScreenOnWhilePlaying(true)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "onStartCommand()")
        if (MainApp.instance.sharedPreferences?.getString("statusFCM", "") != "") {
            showNotification(
                MainApp.instance.sharedPreferences?.getString("statusFCM", ""),
                MainApp.instance.sharedPreferences?.getString("contentFCM", ""), "nfm"
            )
        } else {
            showNotification(
                MainApp.instance.sharedPreferences?.getString("titleFCM", ""),
                MainApp.instance.sharedPreferences?.getString("woIDFCM", ""), "wo"
            )
        }
        Log.i(TAG, "onStartCommand() Start!...")
        player?.start()

        serviceScope.launch {
            while (true) {
                delay(1_000)
                mAudioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
            }
        }

        //re-create the service if it is killed.
        return START_STICKY
    }

    fun onStop() {
        Log.i(TAG, "onStop()")
        player?.pause()
        player?.release()
        serviceScope.cancel()
    }

    fun onPause() {
        Log.i(TAG, "onPause()")
        player?.pause()
        player?.release()
        serviceScope.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        player?.pause()
        player?.release()
        serviceScope.cancel()
    }

    override fun onLowMemory() {
        Log.i(TAG, "onLowMemory()")
    }

    //Inside AndroidManifest.xml add android:stopWithTask="false" to the Service definition.
    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, javaClass)
        restartServiceIntent.setPackage(packageName)
        startService(restartServiceIntent)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun showNotification(title: String?, body: String?, type: String?) {
//        val m: Int = random.nextInt(9999 - 1000) + 1000
        val m = (Date().time / 1000L % Int.MAX_VALUE).toInt()

        val channelId = "fcm_smartops"
        val label = "SmartOps"
        val importance = NotificationManager.IMPORTANCE_HIGH
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(channelId, label, importance).apply {
                setSound(null, null)
                notificationManager.createNotificationChannel(this)
            }
        }

        val itn = Intent(applicationContext, HomepageActivity::class.java)
        if (type == "wo") {
            itn.putExtra("wo", MainApp.instance.sharedPreferences?.getString("titleFCM", ""))
            itn.putExtra("woID", MainApp.instance.sharedPreferences?.getString("woIDFCM", ""))
            itn.putExtra("incwoID", MainApp.instance.sharedPreferences?.getString("incwoIDFCM", ""))
        } else if (type == "nfm") {
            itn.putExtra("location", MainApp.instance.sharedPreferences?.getString("locationFCM", ""))
            itn.putExtra("status", MainApp.instance.sharedPreferences?.getString("statusFCM", ""))
            itn.putExtra("content", MainApp.instance.sharedPreferences?.getString("contentFCM", ""))
            itn.putExtra("raised", MainApp.instance.sharedPreferences?.getString("raisedFCM", ""))
            itn.putExtra("ne_id", MainApp.instance.sharedPreferences?.getString("ne_idFCM", ""))
        }
        itn.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        application.startActivity(itn)

        val intent = Intent(applicationContext, HomepageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        val extras = Bundle()
        if (type == "wo") {
            extras.putString("wo", MainApp.instance.sharedPreferences?.getString("titleFCM", ""))
            extras.putString("woID", MainApp.instance.sharedPreferences?.getString("woIDFCM", ""))
            extras.putString("incwoID", MainApp.instance.sharedPreferences?.getString("incwoIDFCM", ""))
        } else if (type == "nfm") {
            extras.putString("location", MainApp.instance.sharedPreferences?.getString("locationFCM", ""))
            extras.putString("status", MainApp.instance.sharedPreferences?.getString("statusFCM", ""))
            extras.putString("content", MainApp.instance.sharedPreferences?.getString("contentFCM", ""))
            extras.putString("raised", MainApp.instance.sharedPreferences?.getString("raisedFCM", ""))
            extras.putString("ne_id", MainApp.instance.sharedPreferences?.getString("ne_idFCM", ""))
        }
        intent.putExtras(extras)
        intent.action = Intent.ACTION_VIEW

        val flag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val contentIntent = PendingIntent.getActivity(this, m, intent, flag)

        val notification = NotificationCompat.Builder(baseContext, channelId)
            .setContentIntent(contentIntent)
            .setContentTitle(title)
            .setContentText(body)
            .setFullScreenIntent(contentIntent, true)
            .setSmallIcon(R.mipmap.ic_launcher_smartops)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setAutoCancel(true)
            .setOngoing(true)
            .build()

        val notification2 = NotificationCompat.Builder(baseContext, "fcm_smartops")
            .setContentTitle("SmartOps Notification Running...")
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()

//        notificationManager.notify(m, notification)

        startForeground(13, notification)
    }

    companion object {
        private const val TAG = "BackgroundSoundService"
    }
}