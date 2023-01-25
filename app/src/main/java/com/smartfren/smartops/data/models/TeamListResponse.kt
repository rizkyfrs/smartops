package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TeamListResponse {
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

        @SerializedName("team")
        @Expose
        var team: List<Team>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Team {
        @SerializedName("userID")
        @Expose
        var userID: String? = null

        @SerializedName("userFullName")
        @Expose
        var userFullName: String? = null

        @SerializedName("userMail")
        @Expose
        var userMail: String? = null
    }
}