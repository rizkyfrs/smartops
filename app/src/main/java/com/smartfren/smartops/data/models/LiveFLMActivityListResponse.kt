package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LiveFLMActivityListResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("mv_dactlivemon")
    @Expose
    val mvDactlivemon: List<MvDactlivemon>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class MvDactlivemon {
        @SerializedName("dact_num")
        @Expose
        var dactNum: String? = null

        @SerializedName("dact_domain")
        @Expose
        var dactDomain: String? = null

        @SerializedName("dact_activity_name")
        @Expose
        var dactActivityName: String? = null

        @SerializedName("dact_site_id")
        @Expose
        var dactSiteId: String? = null

        @SerializedName("dact_priority")
        @Expose
        var dactPriority: String? = null

        @SerializedName("dact_planned_date")
        @Expose
        var dactPlannedDate: String? = null

        @SerializedName("dact_pic")
        @Expose
        var dactPic: String? = null

        @SerializedName("dact_status")
        @Expose
        var dactStatus: String? = null

        @SerializedName("dact_updatetime")
        @Expose
        var dactUpdatetime: String? = null
    }
}