package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CCFNMonthlyResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_repccf_nmonthly")
    @Expose
    val cvRepccfNmonthly: List<CvRepccfNmonthly>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvRepccfNmonthly {
        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("A20")
        @Expose
        var a20: String? = null

        @SerializedName("A23")
        @Expose
        var a23: String? = null

        @SerializedName("A24")
        @Expose
        var a24: String? = null

        @SerializedName("A31")
        @Expose
        var a31: String? = null

        @SerializedName("A32")
        @Expose
        var a32: String? = null
    }
}