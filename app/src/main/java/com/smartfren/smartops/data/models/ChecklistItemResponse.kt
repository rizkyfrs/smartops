package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ChecklistItemResponse {

    @SerializedName("report")
    @Expose
    private var report: Report? = null

    fun getReport(): Report? {
        return report
    }

    fun setReport(report: Report?) {
        this.report = report
    }

    class Option {
        @SerializedName("optionID")
        @Expose
        var optionID: String? = null

        @SerializedName("optionDesc")
        @Expose
        var optionDesc: String? = null
    }

    class Report {
        @SerializedName("success")
        @Expose
        var success: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("options")
        @Expose
        var options: List<Option>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }
}