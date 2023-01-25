package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteStolenKRIResponse {
    @SerializedName("report")
    @Expose
    var report: Report? = null

    class Info {
        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("region_name")
        @Expose
        var regionName: String? = null

        @SerializedName("site_count")
        @Expose
        var siteCount: String? = null

        @SerializedName("total_stolen")
        @Expose
        var totalStolen: String? = null

        @SerializedName("battery")
        @Expose
        var battery: String? = null

        @SerializedName("modules")
        @Expose
        var modules: String? = null

        @SerializedName("combination")
        @Expose
        var combination: String? = null

        @SerializedName("other")
        @Expose
        var other: String? = null

        @SerializedName("kri")
        @Expose
        var kri: String? = null

        @SerializedName("kri_effectiveness")
        @Expose
        var kriEffectiveness: String? = null
    }

    class Report {
        @SerializedName("success")
        @Expose
        var success: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("info")
        @Expose
        var info: List<Info>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

}