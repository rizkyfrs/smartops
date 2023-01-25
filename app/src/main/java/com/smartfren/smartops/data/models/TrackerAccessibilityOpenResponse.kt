package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerAccessibilityOpenResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("v_site_accessibility_tracker_open")
    @Expose
    var vSiteAccessibilityTrackerOpen: List<VSiteAccessibilityTrackerOpen>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class VSiteAccessibilityTrackerOpen {
        @SerializedName("region_nw")
        @Expose
        var regionNw: String? = null

        @SerializedName("access_block")
        @Expose
        var accessBlock: String? = null

        @SerializedName("danger_night")
        @Expose
        var dangerNight: String? = null

        @SerializedName("not_24_7_hours")
        @Expose
        var not247Hours: String? = null

        @SerializedName("nOpen")
        @Expose
        var nOpen: String? = null
    }
}