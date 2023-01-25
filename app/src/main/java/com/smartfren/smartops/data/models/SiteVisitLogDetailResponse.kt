package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.smartfren.smartops.data.models.SiteVisitLogRepose.CvSvlogPersonal


class SiteVisitLogDetailResponse {
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

        @SerializedName("sv_device_id")
        @Expose
        var svDeviceId: String? = null

        @SerializedName("sv_device_imei")
        @Expose
        var svDeviceImei: String? = null

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

        @SerializedName("sv_updatetime")
        @Expose
        var svUpdatetime: String? = null

        @SerializedName("sv_lastupdate")
        @Expose
        var svLastupdate: Any? = null

        @SerializedName("sv_lastupdatetime")
        @Expose
        var svLastupdatetime: Any? = null

        @SerializedName("sv_long")
        @Expose
        var svLong: Any? = null

        @SerializedName("sv_lat")
        @Expose
        var svLat: Any? = null

        @SerializedName("sv_activity_notes")
        @Expose
        var svActivityNotes: Any? = null

        @SerializedName("sv_activity")
        @Expose
        var svActivity: Any? = null

        @SerializedName("sv_deviation")
        @Expose
        var svDeviation: Any? = null
    }
}