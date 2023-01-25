package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerGroupLevel2Response {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_apireff_trackergroup")
    @Expose
    val cvApireffTrackerGroup2: List<CvApireffTrackerGroup2>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvApireffTrackerGroup2 {
        @SerializedName("param_id")
        @Expose
        var paramId: String? = null

        @SerializedName("param_parent_id")
        @Expose
        var paramParentId: String? = null

        @SerializedName("param_value")
        @Expose
        var paramValue: String? = null
    }
}