package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerBatteryBATTSecureResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("crs_batt_secure_status")
    @Expose
    var crsBattSecureStatus: List<CrsBattSecureStatus>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class CrsBattSecureStatus {
        @SerializedName("rectifier1_bu")
        @Expose
        var rectifier1Bu: String? = null

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