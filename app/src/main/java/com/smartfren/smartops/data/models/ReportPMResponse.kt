package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReportPMResponse {
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

        @SerializedName("report")
        @Expose
        var report: List<Report__1>? = null
    }

    class Report__1 {
        @SerializedName("tpReportNum")
        @Expose
        var tpReportNum: String? = null

        @SerializedName("towerProviderID")
        @Expose
        var towerProviderID: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("siteID")
        @Expose
        var siteID: String? = null

        @SerializedName("periodMonth")
        @Expose
        var periodMonth: String? = null

        @SerializedName("periodYear")
        @Expose
        var periodYear: String? = null

        @SerializedName("siteVisitDate")
        @Expose
        var siteVisitDate: Any? = null

        @SerializedName("KWHReading")
        @Expose
        var kWHReading: String? = null

        @SerializedName("TPMaintenanceReport")
        @Expose
        var tPMaintenanceReport: String? = null

        @SerializedName("registerDate")
        @Expose
        var registerDate: String? = null
    }
}



