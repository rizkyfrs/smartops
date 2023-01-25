package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActivityReportGroupTypeResponse {
    @SerializedName("report")
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

        @SerializedName("options")
        @Expose
        var options: List<Option>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Option {
        @SerializedName("activityID")
        @Expose
        var activityID: String? = null

        @SerializedName("activityType")
        @Expose
        var activityType: String? = null
    }
}