package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class CCFYearlyResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_repccf_yearly")
    @Expose
    val cvRepccfYearly: List<CvRepccfYearly>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvRepccfYearly {
        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("A15")
        @Expose
        var a15: String? = null

        @SerializedName("A17")
        @Expose
        var a17: String? = null

        @SerializedName("A18")
        @Expose
        var a18: String? = null

        @SerializedName("A21")
        @Expose
        var a21: String? = null

        @SerializedName("A25")
        @Expose
        var a25: String? = null

        @SerializedName("A26")
        @Expose
        var a26: String? = null

        @SerializedName("A33")
        @Expose
        var a33: String? = null

        @SerializedName("A34")
        @Expose
        var a34: String? = null
    }

}