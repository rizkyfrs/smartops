package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ReferenceSiteResponse {
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
        @SerializedName("site_id")
        @Expose
        var siteId: String? = null
    }
}