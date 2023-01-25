package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ClusteringReportResponse {
    @SerializedName("report")
    @Expose
    var report: Report? = null

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

    class Info {
        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("clusterCount")
        @Expose
        var clusterCount: String? = null

        @SerializedName("FLMCount")
        @Expose
        var fLMCount: String? = null

        @SerializedName("TechCount")
        @Expose
        var techCount: String? = null

        @SerializedName("FLMvacant")
        @Expose
        var fLMvacant: String? = null

        @SerializedName("TechVacant")
        @Expose
        var techVacant: String? = null
    }
}
