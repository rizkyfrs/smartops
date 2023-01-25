package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StolenViewReportResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("report")
    @Expose
    val report: Report? = null

    @SerializedName("cv_stolen_report")
    @Expose
    val cvStolenReport: CvStolenReport? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvStolenReport {
        @SerializedName("sr_num")
        @Expose
        val srNum: String? = null

        @SerializedName("sr_region")
        @Expose
        val srRegion: String? = null

        @SerializedName("sr_site_id")
        @Expose
        val srSiteId: String? = null

        @SerializedName("sr_date_of_loss")
        @Expose
        val srDateOfLoss: String? = null

        @SerializedName("sr_existing_security")
        @Expose
        val srExistingSecurity: String? = null

        @SerializedName("sr_stolen_material")
        @Expose
        val srStolenMaterial: String? = null

        @SerializedName("sr_photo")
        @Expose
        val srPhoto: String? = null

        @SerializedName("sr_alarm_name")
        @Expose
        val srAlarmName: String? = null

        @SerializedName("sr_kronologi")
        @Expose
        val srKronologi: String? = null

        @SerializedName("sr_doc_kronologi")
        @Expose
        val srDocKronologi: String? = null

        @SerializedName("sr_lkp")
        @Expose
        val srLkp: String? = null

        @SerializedName("sr_doc_lkp")
        @Expose
        val srDocLkp: String? = null

        @SerializedName("sr_recovery")
        @Expose
        val srRecovery: String? = null

        @SerializedName("sr_investigation")
        @Expose
        val srInvestigation: String? = null

        @SerializedName("sr_doc_investigation")
        @Expose
        val srDocInvestigation: String? = null

        @SerializedName("sr_registrars")
        @Expose
        val srRegistrars: String? = null

        @SerializedName("sr_registerdate")
        @Expose
        val srRegisterdate: String? = null

        @SerializedName("sr_update")
        @Expose
        val srUpdate: Any? = null

        @SerializedName("sr_updatetime")
        @Expose
        val srUpdatetime: Any? = null
    }

    class Report {
        @SerializedName("success")
        @Expose
        var success: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null
    }
}