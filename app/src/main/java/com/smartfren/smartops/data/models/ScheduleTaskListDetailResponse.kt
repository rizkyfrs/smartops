package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class ScheduleTaskListDetailResponse {

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
        @SerializedName("dactNum")
        @Expose
        var dactNum: String? = null

        @SerializedName("dactDomainLev1")
        @Expose
        var dactDomainLev1: String? = null

        @SerializedName("dactDomainLev2")
        @Expose
        var dactDomainLev2: String? = null

        @SerializedName("dactSiteID")
        @Expose
        var dactSiteID: String? = null

        @SerializedName("dactPriority")
        @Expose
        var dactPriority: String? = null

        @SerializedName("dactPlannedDate")
        @Expose
        var dactPlannedDate: String? = null

        @SerializedName("dactStatus")
        @Expose
        var dactStatus: String? = null

        @SerializedName("dactPICID")
        @Expose
        var dactPICID: String? = null
    }
}