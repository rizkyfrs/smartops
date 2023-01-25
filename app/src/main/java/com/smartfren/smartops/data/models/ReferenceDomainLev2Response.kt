package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ReferenceDomainLev2Response {
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

        @SerializedName("activity")
        @Expose
        var activity: List<Activity>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Activity {
        @SerializedName("activityName")
        @Expose
        var activityName: String? = null
    }

}