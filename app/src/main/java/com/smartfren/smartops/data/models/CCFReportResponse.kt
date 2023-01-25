package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CCFReportResponse {
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

        @SerializedName("total data")
        @Expose
        var totalData: String? = null

        @SerializedName("total page")
        @Expose
        var totalPage: String? = null

        @SerializedName("page number")
        @Expose
        var pageNumber: String? = null

        @SerializedName("activities")
        @Expose
        var activities: List<Activity>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Activity {
        @SerializedName("activityNum")
        @Expose
        var activityNum: String? = null

        @SerializedName("activityID")
        @Expose
        var activityID: String? = null

        @SerializedName("region")
        @Expose
        var region: String? = null

        @SerializedName("activityLoc")
        @Expose
        var activityLocation: String? = null

        @SerializedName("activityType")
        @Expose
        var activityType: String? = null

        @SerializedName("activityCategory")
        @Expose
        var activityCategory: String? = null

        @SerializedName("activityDate")
        @Expose
        var activityDate: String? = null

        @SerializedName("activityPIC")
        @Expose
        var activityPIC: String? = null

        @SerializedName("activityReport")
        @Expose
        var activityReport: String? = null

        @SerializedName("validationCCF")
        @Expose
        var validationCCF: String? = null

        @SerializedName("validationSME")
        @Expose
        var validationSME: String? = null
    }
}