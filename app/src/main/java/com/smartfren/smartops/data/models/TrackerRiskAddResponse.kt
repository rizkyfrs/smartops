package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerRiskAddResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_regrtracker_ops")
    @Expose
    val cvRegrtrackerOps: CvRegrtrackerOps? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvRegrtrackerOps {
        @SerializedName("tt_group_level_1")
        @Expose
        var ttGroupLevel1: String? = null

        @SerializedName("tt_group_level_2")
        @Expose
        var ttGroupLevel2: String? = null

        @SerializedName("region")
        @Expose
        var region: Int? = null

        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("desc")
        @Expose
        var desc: String? = null

        @SerializedName("pic")
        @Expose
        var pic: String? = null

        @SerializedName("action_desc")
        @Expose
        var actionDesc: String? = null

        @SerializedName("report_date")
        @Expose
        var reportDate: String? = null

        @SerializedName("target_date")
        @Expose
        var targetDate: String? = null

        @SerializedName("tt_status")
        @Expose
        var ttStatus: String? = null

        @SerializedName("created_by")
        @Expose
        var createdBy: String? = null

        @SerializedName("created_on")
        @Expose
        var createdOn: String? = null

        @SerializedName("rtt_num")
        @Expose
        var rttNum: String? = null
    }
}