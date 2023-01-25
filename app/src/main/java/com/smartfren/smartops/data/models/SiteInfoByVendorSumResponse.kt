package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteInfoByVendorSumResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("mvrepsitevendorsum")
    @Expose
    var mvrepsitevendorsum: List<Mvrepsitevendorsum>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class Mvrepsitevendorsum {
        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("AIRSPAN")
        @Expose
        var airspan: String? = null

        @SerializedName("FIBERHOME")
        @Expose
        var fiberhome: String? = null

        @SerializedName("NOKIA")
        @Expose
        var nokia: String? = null

        @SerializedName("ZTE")
        @Expose
        var zte: String? = null
    }
}