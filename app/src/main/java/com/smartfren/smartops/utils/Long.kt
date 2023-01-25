package com.smartfren.smartops.utils

import android.text.format.DateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.roundToInt

fun Long.formatStopwatchTime(useLongerMSFormat: Boolean): String {
    val MSFormat = if (useLongerMSFormat) "%03d" else "%01d"
    val hours = TimeUnit.MILLISECONDS.toHours(this)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(hours)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this))
    var ms = this % 1000
    if (!useLongerMSFormat) {
        ms /= 100
    }
    val format = "%02d:%02d:%02d"
    return String.format(format, hours, minutes, seconds)

//    return when {
//        hours > 0 -> {
//            val format = "%02d:%02d:%02d"
//            String.format(format, hours, minutes, seconds)
//        }
//        minutes > 0 -> {
//            val format = "%02d:%02d:%02d"
//            String.format(format, hours, minutes, seconds)
//        }
//        else -> {
//            val format = "%02d:%02d:%02d"
//            String.format(format, hours, minutes, seconds)
//        }
//    }
}

fun Long.timestampFormat(format: String = "dd. MM. yyyy"): String {
    val calendar = Calendar.getInstance(Locale.getDefault())
    calendar.timeInMillis = this

    return DateFormat.format(format, calendar).toString()
}

fun Long.getFormattedDuration(forceShowHours: Boolean = false): String {
    return this.div(1000F).roundToInt().getFormattedDuration(forceShowHours)
}

val Long.secondsToMillis get() = TimeUnit.SECONDS.toMillis(this)
val Long.millisToSeconds get() = TimeUnit.MILLISECONDS.toSeconds(this)

fun Int.getFormattedDuration(forceShowHours: Boolean = false): String {
    val sb = StringBuilder(8)
    val hours = this / 3600
    val minutes = this % 3600 / 60
    val seconds = this % 60

    if (this >= 3600) {
        sb.append(String.format(Locale.getDefault(), "%02d", hours)).append(":")
    } else if (forceShowHours) {
        sb.append("0:")
    }

    sb.append(String.format(Locale.getDefault(), "%02d", minutes))
    sb.append(":").append(String.format(Locale.getDefault(), "%02d", seconds))
    return sb.toString()
}