package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StolenReportResponse {
    @SerializedName("report")
    @Expose
    var report: Report? = null

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

    class Info {
        @SerializedName("stolenhist_num")
        @Expose
        var stolenhistNum: String? = null

        @SerializedName("stolen_mon")
        @Expose
        var stolenMon: String? = null

        @SerializedName("hyearsum")
        @Expose
        var hyearsum: String? = null

        @SerializedName("cyearsum")
        @Expose
        var cyearsum: String? = null
    }
}