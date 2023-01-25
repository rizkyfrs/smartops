package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerLowFuelResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("v_site_activity_low_fuel_alarm_sensor")
    @Expose
    var vSiteActivityLowFuelAlarmSensor: List<VSiteActivityLowFuelAlarmSensor>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class VSiteActivityLowFuelAlarmSensor {
        @SerializedName("region_nw")
        @Expose
        var regionNw: String? = null

        @SerializedName("dl_close")
        @Expose
        var dlClose: String? = null

        @SerializedName("dl_open")
        @Expose
        var dlOpen: String? = null

        @SerializedName("dl_total")
        @Expose
        var dlTotal: String? = null

        @SerializedName("ibs_close")
        @Expose
        var ibsClose: String? = null

        @SerializedName("ibs_open")
        @Expose
        var ibsOpen: String? = null

        @SerializedName("ibs_total")
        @Expose
        var ibsTotal: String? = null

        @SerializedName("other_close")
        @Expose
        var otherClose: String? = null

        @SerializedName("other_open")
        @Expose
        var otherOpen: String? = null

        @SerializedName("other_total")
        @Expose
        var otherTotal: String? = null

        @SerializedName("total")
        @Expose
        var total: String? = null

        @SerializedName("dl_progress")
        @Expose
        var dlProgress: String? = null

        @SerializedName("ibs_progress")
        @Expose
        var ibsProgress: String? = null

        @SerializedName("other_progress")
        @Expose
        var otherProgress: String? = null
    }
}