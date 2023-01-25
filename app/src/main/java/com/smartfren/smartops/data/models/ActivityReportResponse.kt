package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActivityReportResponse {

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

        @SerializedName("activityGroup")
        @Expose
        var activityGroup: String? = null

        @SerializedName("activityType")
        @Expose
        var activityType: String? = null

        @SerializedName("elementAvailability")
        @Expose
        var elementAvailability: String? = null

        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("siteID")
        @Expose
        var siteID: String? = null

        @SerializedName("activityDate")
        @Expose
        var activityDate: String? = null

        @SerializedName("PICID")
        @Expose
        var picid: String? = null

        @SerializedName("PICName")
        @Expose
        var pICName: String? = null

        @SerializedName("activityStatus")
        @Expose
        var activityStatus: String? = null

        @SerializedName("activityNotes")
        @Expose
        var activityNotes: String? = null

        @SerializedName("createdBy")
        @Expose
        var createdBy: String? = null

        @SerializedName("createdOn")
        @Expose
        var createdOn: String? = null
    }
}