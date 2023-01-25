package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CheckInResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("cv_svlog_personal")
    @Expose
    var cvSvlogPersonal: CvSvlogPersonal? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvSvlogPersonal {
        @SerializedName("sv_date")
        @Expose
        var svDate: String? = null

        @SerializedName("sv_reg")
        @Expose
        var svReg: Int? = null

        @SerializedName("sv_site")
        @Expose
        var svSite: String? = null

        @SerializedName("sv_long")
        @Expose
        var svLong: String? = null

        @SerializedName("sv_lat")
        @Expose
        var svLat: String? = null

        @SerializedName("sv_device_imei")
        @Expose
        var svDeviceImei: String? = null

        @SerializedName("sv_device_custodian")
        @Expose
        var svDeviceCustodian: Int? = null

        @SerializedName("sv_num")
        @Expose
        var svNum: String? = null
    }
}