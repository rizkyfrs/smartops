package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CCFWeeklyResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_repccf_weekly")
    @Expose
    val cvRepccfWeekly: List<CvRepccfWeekly>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvRepccfWeekly {
        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("A02")
        @Expose
        var a02: String? = null

        @SerializedName("A03")
        @Expose
        var a03: String? = null

        @SerializedName("A05")
        @Expose
        var a05: String? = null

        @SerializedName("A29")
        @Expose
        var a29: String? = null
    }

}