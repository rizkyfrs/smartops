package com.smartfren.smartops

import android.app.Application
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.smartfren.smartops.ui.login.LoginActivity
import com.smartfren.smartops.utils.Stopwatch
import com.smartfren.smartops.utils.StopwatchStopService
import com.smartfren.smartops.utils.startStopwatchService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.greenrobot.eventbus.EventBus

@ExperimentalCoroutinesApi
@HiltAndroidApp
class MainApp : Application(), LifecycleObserver {

    var sharedPreferences: SharedPreferences? = null

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)

        instance = this
        sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)
    }

    fun onSessionExpired(context: Context) {
        sharedPreferences?.edit()?.clear()?.apply()
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackgrounded() {
        if (Stopwatch.state == Stopwatch.State.RUNNING) {
            startStopwatchService(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        if (Stopwatch.state == Stopwatch.State.RUNNING) {
            EventBus.getDefault().post(StopwatchStopService)
        }
    }

    companion object {
        const val CHANNEL_ID = "channel1"
        const val PLAY = "play"
        const val EXIT = "exit"
        lateinit var instance: MainApp
    }
}