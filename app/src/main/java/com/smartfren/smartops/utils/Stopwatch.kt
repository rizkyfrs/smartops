package com.smartfren.smartops.utils

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import java.util.concurrent.CopyOnWriteArraySet

private const val UPDATE_INTERVAL = 10L

object Stopwatch {

    private val updateHandler = Handler(Looper.getMainLooper())
    private var uptimeAtStart = 0L
    private var totalTicks = 0
    private var currentTicks = 0    // ticks that reset at pause
    private var lapTicks = 0
    private var currentLap = 1
    private var currentAlarm = 1800000
    private var schedule = false

    var state = State.STOPPED
        private set(value) {
            field = value
            for (listener in updateListeners) {
                listener.onStateChanged(value)
            }
        }
    private var updateListeners = CopyOnWriteArraySet<UpdateListener>()

    fun reset() {
        updateHandler.removeCallbacksAndMessages(null)
        state = State.STOPPED
        currentTicks = 0
        totalTicks = 0
        currentLap = 1
        lapTicks = 0
    }

    fun toggle(setUptimeAtStart: Boolean) {
        if (state != State.RUNNING) {
            state = State.RUNNING
            updateHandler.post(updateRunnable)
            if (setUptimeAtStart) {
                uptimeAtStart = SystemClock.uptimeMillis()
            }
        } else {
            state = State.PAUSED
            val prevSessionsMS = (totalTicks - currentTicks) * UPDATE_INTERVAL
            val totalDuration = SystemClock.uptimeMillis() - uptimeAtStart + prevSessionsMS
            updateHandler.removeCallbacksAndMessages(null)
            currentTicks = 0
            totalTicks--
            for (listener in updateListeners) {
                listener.onUpdate(totalDuration,  true, schedule)
            }
        }
    }

    /**
     * Add a update listener to the stopwatch. The listener gets the current state
     * immediately after adding. To avoid memory leaks the listener should be removed
     * from the stopwatch.
     * @param updateListener the listener
     */
    fun addUpdateListener(updateListener: UpdateListener) {
        updateListeners.add(updateListener)
        updateListener.onUpdate(
            totalTicks * UPDATE_INTERVAL,
            state != State.STOPPED
        , schedule)
        updateListener.onStateChanged(state)
    }

    /**
     * Remove the listener from the stopwatch
     * @param updateListener the listener
     */
    fun removeUpdateListener(updateListener: UpdateListener) {
        updateListeners.remove(updateListener)
    }

    private val updateRunnable = object : Runnable {
        override fun run() {
            if (state == State.RUNNING) {
                if (totalTicks % 10 == 0) {
                    for (listener in updateListeners) {
                        listener.onUpdate(
                            totalTicks * UPDATE_INTERVAL,
                            false,
                            schedule
                        )
                    }
                }
                totalTicks++
                currentTicks++
                lapTicks++
                updateHandler.postAtTime(this, uptimeAtStart + currentTicks * UPDATE_INTERVAL)
                if (currentTicks == currentAlarm) {
                    currentAlarm += 1800000
                    schedule = true
                } else {
                    schedule = false
                }
            }
        }
    }

    enum class State {
        RUNNING,
        PAUSED,
        STOPPED
    }

    interface UpdateListener {
        fun onUpdate(totalTime: Long, useLongerMSFormat: Boolean, schedule: Boolean)
        fun onStateChanged(state: State)
    }
}
