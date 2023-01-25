package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteInfoBySiteModelSumResponse {

    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("mvrepsitemodel")
    @Expose
    val mvrepsitemodel: List<Mvrepsitemodel>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class Mvrepsitemodel {
        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("GF")
        @Expose
        var gf: String? = null

        @SerializedName("RT")
        @Expose
        var rt: String? = null

        @SerializedName("COW")
        @Expose
        var cow: String? = null

        @SerializedName("NA")
        @Expose
        var na: String? = null

        @SerializedName("totalCount")
        @Expose
        var totalCount: String? = null
    }
}