package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WebCHAvailabilityResponse {
    @Expose
    private var report: Report? = null

    fun getReport(): Report? {
        return report
    }

    fun setReport(report: Report?) {
        this.report = report
    }

    class Report {
        @SerializedName("success")
        @Expose
        var success: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("data")
        @Expose
        var data: List<Datum>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Datum {
        @SerializedName("period")
        @Expose
        var period: String? = null

        @SerializedName("avail")
        @Expose
        var avail: Double? = null

        @SerializedName("populate")
        @Expose
        var populate: Double? = null
    }

}