package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteInfoByFGSSumResponse {

    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("mvrepsitefgs")
    @Expose
    val mvrepsitefgs: List<Mvrepsitefg>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class Mvrepsitefg {
        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("NO-FGS")
        @Expose
        var noFgs: String? = null

        @SerializedName("FGS-TP")
        @Expose
        var fgsTp: String? = null

        @SerializedName("FGS-SF")
        @Expose
        var fgsSf: String? = null

        @SerializedName("totalCount")
        @Expose
        var totalCount: String? = null
    }
}