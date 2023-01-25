package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerGroupLevel1Response {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_apireff_trackergroup")
    @Expose
    val cvApireffTrackerGroup1: List<CvApireffTrackerGroup1>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvApireffTrackerGroup1 {
        @SerializedName("param_id")
        @Expose
        var paramId: kotlin.String? = null

        @SerializedName("param_value")
        @Expose
        var paramValue: kotlin.String? = null
    }
}