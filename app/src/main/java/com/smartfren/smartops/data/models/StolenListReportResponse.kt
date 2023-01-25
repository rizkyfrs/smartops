package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StolenListReportResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_stolen_report")
    @Expose
    val cvStolenReport: List<CvStolenReport>? = null

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
        var srNum: String? = null

        @SerializedName("sr_region")
        @Expose
        var srRegion: String? = null

        @SerializedName("sr_site_id")
        @Expose
        var srSiteId: String? = null

        @SerializedName("sr_date_of_loss")
        @Expose
        var srDateOfLoss: String? = null

        @SerializedName("sr_existing_security")
        @Expose
        var srExistingSecurity: String? = null

        @SerializedName("sr_doc_kronologi")
        @Expose
        var srDocKronologi: String? = null

        @SerializedName("sr_doc_lkp")
        @Expose
        var srDocLkp: String? = null

        @SerializedName("sr_doc_investigation")
        @Expose
        var srDocInvestigation: String? = null

        @SerializedName("sr_registrars")
        @Expose
        var srRegistrars: String? = null

        @SerializedName("sr_registerdate")
        @Expose
        var srRegisterdate: String? = null

        @SerializedName("sr_update")
        @Expose
        var srUpdate: Any? = null

        @SerializedName("sr_updatetime")
        @Expose
        var srUpdatetime: Any? = null
    }
}