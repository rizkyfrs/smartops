package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ActivityChecklistResponse {
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

        @SerializedName("checklist")
        @Expose
        var checklist: List<Check>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

    class Check {
        @SerializedName("activityResultNum")
        @Expose
        var activityResultNum: String? = null

        @SerializedName("activityFormID")
        @Expose
        var activityFormID: String? = null

        @SerializedName("activityID")
        @Expose
        var activityID: String? = null

        @SerializedName("activityName")
        @Expose
        var activityName: String? = null

        @SerializedName("checkListID")
        @Expose
        var checkListID: String? = null

        @SerializedName("checkListGroup")
        @Expose
        var checkListGroup: String? = null

        @SerializedName("checkListSeqNum")
        @Expose
        var checkListSeqNum: String? = null

        @SerializedName("checkListItem")
        @Expose
        var checkListItem: String? = null

        @SerializedName("resultOption")
        @Expose
        var resultOption: String? = null

        @SerializedName("resultText")
        @Expose
        var resultText: String? = null

        @SerializedName("resultAtt")
        @Expose
        var resultAtt: String? = null

        @SerializedName("checkListItemStatus")
        @Expose
        var checkListItemStatus: String? = null

        @SerializedName("resultOptionMandatory")
        @Expose
        var resultOptionMandatory: String? = null

        @SerializedName("resultTextMandatory")
        @Expose
        var resultTextMandatory: String? = null

        @SerializedName("resultAttachmentMandatory")
        @Expose
        var resultAttachmentMandatory: String? = null
    }

}