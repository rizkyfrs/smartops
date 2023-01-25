package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TTWOViewResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_fsttwohistRCA")
    @Expose
    val cvFsttwohistRCA: CvFsttwohistRCA? = null

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

        @SerializedName("hist_file_id")
        @Expose
        var histFileId: String? = null

        @SerializedName("region")
        @Expose
        var region: String? = null

        @SerializedName("incident_id")
        @Expose
        var incidentId: String? = null

        @SerializedName("tt_id")
        @Expose
        var ttId: String? = null

        @SerializedName("wo_id")
        @Expose
        var woId: String? = null

        @SerializedName("time_start")
        @Expose
        var timeStart: String? = null

        @SerializedName("time_end")
        @Expose
        var timeEnd: String? = null

        @SerializedName("time_duration")
        @Expose
        var timeDuration: String? = null

        @SerializedName("assignment")
        @Expose
        var assignment: String? = null

        @SerializedName("ne_name")
        @Expose
        var neName: String? = null

        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("domain")
        @Expose
        var domain: String? = null

        @SerializedName("alarm_name")
        @Expose
        var alarmName: String? = null

        @SerializedName("category")
        @Expose
        var category: String? = null

        @SerializedName("service_affecting")
        @Expose
        var serviceAffecting: String? = null

        @SerializedName("wo_description")
        @Expose
        var woDescription: String? = null

        @SerializedName("u_parrent_RCA")
        @Expose
        var uParrentRCA: String? = null

        @SerializedName("u_rca_1")
        @Expose
        var uRca1: String? = null

        @SerializedName("u_rca_2")
        @Expose
        var uRca2: String? = null

        @SerializedName("u_rca_3")
        @Expose
        var uRca3: String? = null

        @SerializedName("u_rca_status")
        @Expose
        var uRcaStatus: Any? = null

        @SerializedName("u_rca_flag")
        @Expose
        var uRcaFlag: String? = null

        @SerializedName("rca")
        @Expose
        var rca: String? = null

        @SerializedName("rca_lev1")
        @Expose
        var rcaLev1: String? = null

        @SerializedName("rca_lev2")
        @Expose
        var rcaLev2: String? = null

        @SerializedName("remarks")
        @Expose
        var remarks: String? = null

        @SerializedName("site_pic_id")
        @Expose
        var sitePicId: String? = null
    }
}