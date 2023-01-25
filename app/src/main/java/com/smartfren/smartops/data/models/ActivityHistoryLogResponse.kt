package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ActivityHistoryLogResponse {
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

        @SerializedName("task")
        @Expose
        var task: List<Task>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Task {
        @SerializedName("actNum")
        @Expose
        var actNum: String? = null

        @SerializedName("ActRegionName")
        @Expose
        var actRegionName: String? = null

        @SerializedName("domainLev1")
        @Expose
        var domainLev1: String? = null

        @SerializedName("domainLev2")
        @Expose
        var domainLev2: String? = null

        @SerializedName("siteID")
        @Expose
        var siteID: String? = null

        @SerializedName("NEID")
        @Expose
        var neid: String? = null

        @SerializedName("vendor")
        @Expose
        var vendor: String? = null

        @SerializedName("WOID")
        @Expose
        var woid: String? = null

        @SerializedName("priority")
        @Expose
        var priority: String? = null

        @SerializedName("actionNeeded")
        @Expose
        var actionNeeded: String? = null

        @SerializedName("plannedDate")
        @Expose
        var plannedDate: String? = null

        @SerializedName("PICName")
        @Expose
        var pICName: String? = null

        @SerializedName("actualDate")
        @Expose
        var actualDate: String? = null

        @SerializedName("RCA1")
        @Expose
        var rca1: String? = null

        @SerializedName("RCA2")
        @Expose
        var rca2: String? = null

        @SerializedName("RCA3")
        @Expose
        var rca3: String? = null

        @SerializedName("RCAAction")
        @Expose
        var rCAAction: String? = null

        @SerializedName("actionDetail")
        @Expose
        var actionDetail: String? = null

        @SerializedName("picBefore")
        @Expose
        var picBefore: String? = null

        @SerializedName("picAfter")
        @Expose
        var picAfter: String? = null

        @SerializedName("actionType")
        @Expose
        var actionType: String? = null

        @SerializedName("taskStatus")
        @Expose
        var taskStatus: String? = null
    }
}