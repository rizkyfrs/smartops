package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NFMAlarmResponse {
    @SerializedName("alarm")
    @Expose
    private var alarm: List<Alarm?>? = null

    fun getAlarm(): List<Alarm?>? {
        return alarm
    }

    fun setAlarm(alarm: List<Alarm?>?) {
        this.alarm = alarm
    }

    class Alarm {
        @SerializedName("alarm")
        @Expose
        var alarm: String? = null

        @SerializedName("raised_time")
        @Expose
        var raisedTime: String? = null
    }
}