package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerRiskGHListResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_regrtracker_val")
    @Expose
    val cvRegrtrackerVal: List<CvRegrtrackerVal>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvRegrtrackerVal {
        @SerializedName("rtt_num")
        @Expose
        var rttNum: String? = null

        @SerializedName("tt_group_level_1")
        @Expose
        var ttGroupLevel1: String? = null

        @SerializedName("tt_group_level_2")
        @Expose
        var ttGroupLevel2: String? = null

        @SerializedName("region")
        @Expose
        var region: String? = null

        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("user_complaint")
        @Expose
        var userComplaint: String? = null

        @SerializedName("pic")
        @Expose
        var pic: String? = null

        @SerializedName("root_cause_desc")
        @Expose
        var rootCauseDesc: String? = null

        @SerializedName("tt_status")
        @Expose
        var ttStatus: String? = null

        @SerializedName("created_by")
        @Expose
        var createdBy: String? = null

        @SerializedName("created_on")
        @Expose
        var createdOn: String? = null

        @SerializedName("updated_by")
        @Expose
        var updatedBy: String? = null

        @SerializedName("updated_on")
        @Expose
        var updatedOn: String? = null
    }
}