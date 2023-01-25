package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ScheduleResponse {

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

        @SerializedName("sites")
        @Expose
        var sites: List<Site>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Site {
        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("siteID")
        @Expose
        var siteID: String? = null

        @SerializedName("userID")
        @Expose
        var userID: String? = null

        @SerializedName("uName")
        @Expose
        var uName: String? = null

        @SerializedName("userMail")
        @Expose
        var userMail: String? = null

        @SerializedName("plannedDate")
        @Expose
        var plannedDate: String? = null

        @SerializedName("actP0")
        @Expose
        var actP0: String? = null

        @SerializedName("actP1")
        @Expose
        var actP1: String? = null

        @SerializedName("actP2")
        @Expose
        var actP2: String? = null

        @SerializedName("totalActivity")
        @Expose
        var totalActivity: String? = null

        @SerializedName("taskUpdated")
        @Expose
        var taskUpdated: String? = null

        @SerializedName("taskPrtg")
        @Expose
        var taskPrtg: String? = null
    }

}