package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SmartopsLoginReportResponse {
    @SerializedName("report")
    @Expose
    var report: Report? = null

    class Info {
        @SerializedName("regionID")
        @Expose
        var regionID: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("clusterCount")
        @Expose
        var clusterCount: String? = null

        @SerializedName("PICCount")
        @Expose
        var pICCount: String? = null

        @SerializedName("SmartOPSActive")
        @Expose
        var smartOPSActive: String? = null

        @SerializedName("SmartOPSInactive")
        @Expose
        var smartOPSInactive: String? = null
    }

    class Report {
        @SerializedName("success")
        @Expose
        var success: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("info")
        @Expose
        var info: List<Info>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }
}
