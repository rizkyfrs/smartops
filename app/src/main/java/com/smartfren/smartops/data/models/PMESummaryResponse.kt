package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PMESummaryResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("cv_postmortem_summary")
    @Expose
    var cvPostmortemSummary: List<CvPostmortemSummary?>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class CvPostmortemSummary {
        @SerializedName("kpi")
        @Expose
        var kpi: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null
    }
}