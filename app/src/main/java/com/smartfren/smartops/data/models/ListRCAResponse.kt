package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListRCAResponse {

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

        @SerializedName("rca")
        @Expose
        var rca: List<Rca>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Rca {
        @SerializedName("rca_num")
        @Expose
        var rcaNum: String? = null

        @SerializedName("rca_name")
        @Expose
        var rcaName: String? = null
        @SerializedName("rca_action")
        @Expose
        var rcaAction: String? = null
    }
}