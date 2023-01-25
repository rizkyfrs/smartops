package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerBatteryBBTRegionResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("crs_bbt_region")
    @Expose
    var crsBbtRegion: List<CrsBbtRegion>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class CrsBbtRegion {
        @SerializedName("urutan")
        @Expose
        var urutan: Int? = null

        @SerializedName("area")
        @Expose
        var area: String? = null

        @SerializedName("batt_stolen")
        @Expose
        var battStolen: Int? = null

        @SerializedName("batt_05")
        @Expose
        var batt05: Int? = null

        @SerializedName("batt_15")
        @Expose
        var batt15: Int? = null

        @SerializedName("batt_25")
        @Expose
        var batt25: Int? = null

        @SerializedName("batt_35")
        @Expose
        var batt35: Int? = null

        @SerializedName("batt_45")
        @Expose
        var batt45: Int? = null

        @SerializedName("batt_no_rectifier")
        @Expose
        var battNoRectifier: Int? = null
    }
}