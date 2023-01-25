package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class SiteProgressResponse {
    @SerializedName("report")
    @Expose
    private var report: Report? = null

    fun getReport(): Report? {
        return report
    }

    fun setReport(report: Report?) {
        this.report = report
    }

    class Info {
        @SerializedName("totalTask")
        @Expose
        var totalTask: String? = null

        @SerializedName("taskOpen")
        @Expose
        var taskOpen: String? = null

        @SerializedName("scheduled")
        @Expose
        var scheduled: String? = null

        @SerializedName("taskUpdated")
        @Expose
        var taskUpdated: String? = null

        @SerializedName("taskPrtg")
        @Expose
        var taskPrtg: String? = null
    }

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
}