package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WorkingDateResponse {
    @SerializedName("report")
    @Expose
    private var report: Report? = null

    fun getReport(): Report? {
        return report
    }

    fun setReport(report: Report?) {
        this.report = report
    }

    class Date {
        @SerializedName("calendar_date")
        @Expose
        var calendarDate: String? = null
    }

    class Report {
        @SerializedName("success")
        @Expose
        var success: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("date")
        @Expose
        var date: List<Date>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

}