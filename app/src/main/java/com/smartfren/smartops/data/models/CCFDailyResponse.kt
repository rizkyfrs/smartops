package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CCFDailyResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_repccf_daily")
    @Expose
    val cvRepccfDaily: List<CvRepccfDaily>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvRepccfDaily {
        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("A01")
        @Expose
        var a01: String? = null

        @SerializedName("A28")
        @Expose
        var a28: String? = null
    }
}