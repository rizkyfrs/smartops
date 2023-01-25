package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteInfoBySiteSumResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("mvrepsitetagsum")
    @Expose
    var mvrepsitetagsum: List<Mvrepsitetagsum>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class Mvrepsitetagsum {
        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("TAG 5")
        @Expose
        var tag5: String? = null

        @SerializedName("TAG 2")
        @Expose
        var tag2: String? = null

        @SerializedName("TAG 4")
        @Expose
        var tag4: String? = null

        @SerializedName("PIC Data")
        @Expose
        var pICData: String? = null

        @SerializedName("RATIO")
        @Expose
        var ratio: String? = null
    }
}