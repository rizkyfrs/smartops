package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class PersonalInfoResponse {
    @SerializedName("report")
    @Expose
    private var report: Report? = null

    fun getReport(): Report? {
        return report
    }

    fun setReport(report: Report?) {
        this.report = report
    }

    class Report {
        @SerializedName("success")
        @Expose
        var success: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("info")
        @Expose
        var info: List<Info>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }
    class Info {
        @SerializedName("IDNum")
        @Expose
        var iDNum: String? = null

        @SerializedName("PICName")
        @Expose
        var pICName: String? = null

        @SerializedName("PICMail")
        @Expose
        var pICMail: String? = null

        @SerializedName("PICNumber")
        @Expose
        var pICNumber: String? = null

        @SerializedName("levelID")
        @Expose
        var levelID: String? = null

        @SerializedName("levelName")
        @Expose
        var levelName: String? = null

        @SerializedName("uActivation")
        @Expose
        var uActivation: String? = null

        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("PICSuperiorName")
        @Expose
        var pICSuperiorName: String? = null

        @SerializedName("PICSuperiorMail")
        @Expose
        var pICSuperiorMail: String? = null

        @SerializedName("PICPhoto")
        @Expose
        var pICPhoto: String? = null

        @SerializedName("PICTagging")
        @Expose
        var pICTagging: String? = null

        @SerializedName("PICTaggingDate")
        @Expose
        var pICTaggingDate: String? = null

        @SerializedName("deviceStatus")
        @Expose
        val deviceStatus: String? = null

        @SerializedName("deviceLastReporDate")
        @Expose
        val deviceLastReporDate: String? = null

        @SerializedName("deviceLocationService")
        @Expose
        val deviceLocationService: String? = null

        @SerializedName("deviceLastLocationReported")
        @Expose
        val deviceLastLocationReported: String? = null

        @SerializedName("deviceLastLocationReportedTime")
        @Expose
        val deviceLastLocationReportedTime: String? = null

        @SerializedName("deviceLastLat")
        @Expose
        val deviceLastLat: String? = null

        @SerializedName("deviceLastLong")
        @Expose
        val deviceLastLong: String? = null
    }
}