package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConsumableUsageReportResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_consflmreport")
    @Expose
    val cvConsflmreport: List<CvConsflmreport>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvConsflmreport {
        @SerializedName("cflm_usage_num")
        @Expose
        var cflmUsageNum: String? = null

        @SerializedName("cflm_handover_id")
        @Expose
        var cflmHandoverId: Any? = null

        @SerializedName("cflm_notes")
        @Expose
        var cflmNotes: String? = null

        @SerializedName("cflm_registrars")
        @Expose
        var cflmRegistrars: String? = null

        @SerializedName("cflm_registerdate")
        @Expose
        var cflmRegisterdate: String? = null

        @SerializedName("cflm_site_id")
        @Expose
        var cflmSiteId: String? = null

        @SerializedName("cflm_site_visit_date")
        @Expose
        var cflmSiteVisitDate: String? = null

        @SerializedName("cflm_cons_type")
        @Expose
        var cflmConsType: String? = null

        @SerializedName("cflm_report_type")
        @Expose
        var cflmReportType: String? = null
    }
}