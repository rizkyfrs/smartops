package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StolenReportCurrentMonthResponse {

    @SerializedName("report")
    @Expose
    var report: Report? = null

    class Info {
        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("region_name")
        @Expose
        var regionName: String? = null

        @SerializedName("battery")
        @Expose
        var battery: String? = null

        @SerializedName("module")
        @Expose
        var module: String? = null

        @SerializedName("combination")
        @Expose
        var combination: String? = null

        @SerializedName("other")
        @Expose
        var other: String? = null

        @SerializedName("total")
        @Expose
        var total: String? = null
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