package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WOMTTRResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("cv_womttr_rep")
    @Expose
    var cvWomttrRep: List<CvWomttrRep?>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvWomttrRep {
        @SerializedName("region")
        @Expose
        var region: String? = null

        @SerializedName("region_name")
        @Expose
        var regionName: String? = null

        @SerializedName("a")
        @Expose
        var a: String? = null

        @SerializedName("b")
        @Expose
        var b: String? = null

        @SerializedName("c")
        @Expose
        var c: String? = null

        @SerializedName("d")
        @Expose
        var d: String? = null

        @SerializedName("e")
        @Expose
        var e: String? = null
    }
}