package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerManagerViewResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("tb_rtracker")
    @Expose
    val tbRtracker: TrackerRiskViewResponse.CvRegrtrackerOps? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null
}