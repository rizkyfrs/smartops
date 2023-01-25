package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class FLMStatisticRepsonse {
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

        @SerializedName("stats")
        @Expose
        var stats: List<Stat>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Stat {
        @SerializedName("distinctiveSite")
        @Expose
        var distinctiveSite: String? = null

        @SerializedName("totalScheduled")
        @Expose
        var totalScheduled: String? = null

        @SerializedName("totalResolved")
        @Expose
        var totalResolved: String? = null

        @SerializedName("totalPending")
        @Expose
        var totalPending: String? = null

        @SerializedName("totalReject")
        @Expose
        var totalReject: String? = null

        @SerializedName("totalClosed")
        @Expose
        var totalClosed: String? = null
    }

}