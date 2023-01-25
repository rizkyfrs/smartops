package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NFMTiketResponse {
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

        @SerializedName("total data")
        @Expose
        var totalData: String? = null

        @SerializedName("total page")
        @Expose
        var totalPage: String? = null

        @SerializedName("page number")
        @Expose
        var pageNumber: String? = null

        @SerializedName("tickets")
        @Expose
        var tickets: List<Ticket>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Ticket {
        @SerializedName("NFMticketNum")
        @Expose
        var nFMticketNum: String? = null

        @SerializedName("NFMticketID")
        @Expose
        var nFMticketID: String? = null

        @SerializedName("NFMIOTNEID")
        @Expose
        var nfmiotneid: String? = null

        @SerializedName("NFMticketStatus")
        @Expose
        var nFMticketStatus: String? = null

        @SerializedName("NFMIP")
        @Expose
        var nfmip: String? = null

        @SerializedName("NFMAlarmCode")
        @Expose
        var nFMAlarmCode: String? = null

        @SerializedName("NFMAlarmValue")
        @Expose
        var nFMAlarmValue: String? = null

        @SerializedName("NFMAlarmRegisterDate")
        @Expose
        var nFMAlarmRegisterDate: String? = null

        @SerializedName("NFMTicketACKStatus")
        @Expose
        var nFMTicketACKStatus: String? = null

        @SerializedName("NFMTicketACKDate")
        @Expose
        var nFMTicketACKDate: String? = null

        @SerializedName("NFMTicketACKPICName")
        @Expose
        var nFMTicketACKPICName: Any? = null
    }
}