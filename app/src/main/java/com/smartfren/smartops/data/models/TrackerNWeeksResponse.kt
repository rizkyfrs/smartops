package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerNWeeksResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("crs_nworst_site")
    @Expose
    var crsNworstSite: List<CrsNworstSite>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class CrsNworstSite {
        @SerializedName("region")
        @Expose
        var region: String? = null

        @SerializedName("vendor_id")
        @Expose
        var vendorId: String? = null

        @SerializedName("fops_mgr")
        @Expose
        var fopsMgr: String? = null

        @SerializedName("fops_ac")
        @Expose
        var fopsAc: String? = null

        @SerializedName("avail")
        @Expose
        var avail: Double? = null

        @SerializedName("good")
        @Expose
        var good: Int? = null

        @SerializedName("average")
        @Expose
        var average: Int? = null

        @SerializedName("worst")
        @Expose
        var worst: Int? = null
    }
}