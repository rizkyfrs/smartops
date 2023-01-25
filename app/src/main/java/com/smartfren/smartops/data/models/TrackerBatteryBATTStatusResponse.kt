package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerBatteryBATTStatusResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("crs_batt_status")
    @Expose
    var crsBattStatus: List<CrsBattStatus>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class CrsBattStatus {
        @SerializedName("rectifier1_status_battery")
        @Expose
        var rectifier1StatusBattery: String? = null

        @SerializedName("jb")
        @Expose
        var jb: Int? = null

        @SerializedName("wj")
        @Expose
        var wj: Int? = null

        @SerializedName("cj")
        @Expose
        var cj: Int? = null

        @SerializedName("ej")
        @Expose
        var ej: Int? = null

        @SerializedName("ns")
        @Expose
        var ns: Int? = null

        @SerializedName("ss")
        @Expose
        var ss: Int? = null

        @SerializedName("sk")
        @Expose
        var sk: Int? = null
    }
}