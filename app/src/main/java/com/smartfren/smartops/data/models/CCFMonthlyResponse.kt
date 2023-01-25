package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CCFMonthlyResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_repccf_monthly")
    @Expose
    val cvRepccfMonthly: List<CvRepccfMonthly>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvRepccfMonthly {
        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("A30")
        @Expose
        var a30: String? = null

        @SerializedName("A35")
        @Expose
        var a35: String? = null
    }
}