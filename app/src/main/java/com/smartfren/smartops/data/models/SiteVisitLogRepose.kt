package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteVisitLogRepose {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("cv_svlog_personal")
    @Expose
    var cvSvlogPersonal: List<CvSvlogPersonal?>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvSvlogPersonal {
        @SerializedName("sv_num")
        @Expose
        var svNum: String? = null

        @SerializedName("sv_date")
        @Expose
        var svDate: String? = null

        @SerializedName("sv_reg")
        @Expose
        var svReg: String? = null

        @SerializedName("sv_site")
        @Expose
        var svSite: String? = null

        @SerializedName("sv_device_group")
        @Expose
        var svDeviceGroup: String? = null

        @SerializedName("sv_device_custodian")
        @Expose
        var svDeviceCustodian: String? = null

        @SerializedName("sv_tmin")
        @Expose
        var svTmin: String? = null

        @SerializedName("sv_tmax")
        @Expose
        var svTmax: String? = null

        @SerializedName("sv_duration")
        @Expose
        var svDuration: String? = null
    }
}