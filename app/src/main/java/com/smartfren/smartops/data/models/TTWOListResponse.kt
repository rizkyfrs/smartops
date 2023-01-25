package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TTWOListResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_fsttwohistRCA")
    @Expose
    val cvFsttwohistRCA: List<CvFsttwohistRCA>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvFsttwohistRCA {
        @SerializedName("hist_num")
        @Expose
        var histNum: String? = null

        @SerializedName("region")
        @Expose
        var region: String? = null

        @SerializedName("wo_id")
        @Expose
        var woId: String? = null

        @SerializedName("time_start")
        @Expose
        var timeStart: String? = null

        @SerializedName("time_end")
        @Expose
        var timeEnd: String? = null

        @SerializedName("assignment")
        @Expose
        var assignment: String? = null

        @SerializedName("ne_name")
        @Expose
        var neName: String? = null

        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("alarm_name")
        @Expose
        var alarmName: String? = null

        @SerializedName("u_rca_1")
        @Expose
        var uRca1: String? = null

        @SerializedName("u_rca_2")
        @Expose
        var uRca2: String? = null

        @SerializedName("u_rca_3")
        @Expose
        var uRca3: String? = null

        @SerializedName("rca_lev2")
        @Expose
        var rcaLev2: String? = null

        @SerializedName("site_pic_id")
        @Expose
        var sitePicId: String? = null
    }
}