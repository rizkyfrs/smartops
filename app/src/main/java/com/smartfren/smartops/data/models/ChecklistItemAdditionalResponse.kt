package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ChecklistItemAdditionalResponse {
    @SerializedName("report")
    @Expose
    var report: Report? = null

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
        @SerializedName("cl_id")
        @Expose
        var clId: String? = null

        @SerializedName("cl_theme_id")
        @Expose
        var clThemeId: String? = null

        @SerializedName("cl_seq_num")
        @Expose
        var clSeqNum: String? = null

        @SerializedName("cl_description")
        @Expose
        var clDescription: String? = null

        @SerializedName("cl_group")
        @Expose
        var clGroup: String? = null

        @SerializedName("cl_multiple_choice_mandatory")
        @Expose
        var clMultipleChoiceMandatory: String? = null

        @SerializedName("cl_attachment_mandatory")
        @Expose
        var clAttachmentMandatory: String? = null

        @SerializedName("cl_notes_mandatory")
        @Expose
        var clNotesMandatory: String? = null

        @SerializedName("cl_status")
        @Expose
        var clStatus: String? = null

        @SerializedName("cl_options")
        @Expose
        var clOptions: String? = null

        @SerializedName("cl_options_extract_status")
        @Expose
        var clOptionsExtractStatus: String? = null

        @SerializedName("cl_registrars")
        @Expose
        var clRegistrars: String? = null

        @SerializedName("cl_registerdate")
        @Expose
        var clRegisterdate: String? = null
    }
}