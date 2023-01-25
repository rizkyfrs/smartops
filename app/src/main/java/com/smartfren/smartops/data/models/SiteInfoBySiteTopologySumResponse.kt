package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteInfoBySiteTopologySumResponse {

    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("mvrepsitetxmode")
    @Expose
    val mvrepsitetxmode: List<Mvrepsitetxmode>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class Mvrepsitetxmode {
        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("EndSite")
        @Expose
        var endSite: String? = null

        @SerializedName("Collector")
        @Expose
        var collector: String? = null

        @SerializedName("SubHub")
        @Expose
        var subHub: String? = null

        @SerializedName("Hub")
        @Expose
        var hub: String? = null

        @SerializedName("SuperHub")
        @Expose
        var superHub: String? = null

        @SerializedName("totalCount")
        @Expose
        var totalCount: String? = null
    }
}