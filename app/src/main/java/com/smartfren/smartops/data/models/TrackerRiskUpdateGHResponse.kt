package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.smartfren.smartops.data.models.TrackerRiskGHListResponse.CvRegrtrackerVal

class TrackerRiskUpdateGHResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_regrtracker_val")
    @Expose
    val cvRegrtrackerVal: CvRegrtrackerVal? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvRegrtrackerVal {
        @SerializedName("close_date")
        @Expose
        var closeDate: String? = null

        @SerializedName("action_taken")
        @Expose
        var actionTaken: String? = null

        @SerializedName("root_cause_desc")
        @Expose
        var rootCauseDesc: String? = null

        @SerializedName("tt_status")
        @Expose
        var ttStatus: String? = null
    }
}