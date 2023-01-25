package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerBatteryResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("v_site_battery_tracker")
    @Expose
    var vSiteBatteryTracker: List<VSiteBatteryTracker?>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class VSiteBatteryTracker {
        @SerializedName("region_nw")
        @Expose
        var regionNw: String? = null

        @SerializedName("total")
        @Expose
        var total: String? = null

        @SerializedName("nClose")
        @Expose
        var nClose: String? = null

        @SerializedName("nOpen")
        @Expose
        var nOpen: String? = null

        @SerializedName("nCancel")
        @Expose
        var nCancel: String? = null

        @SerializedName("super_hub")
        @Expose
        var superHub: String? = null

        @SerializedName("hub")
        @Expose
        var hub: String? = null

        @SerializedName("sub_hub")
        @Expose
        var subHub: String? = null

        @SerializedName("collector")
        @Expose
        var collector: String? = null

        @SerializedName("end_site")
        @Expose
        var endSite: String? = null
    }
}