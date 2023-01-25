package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TTWORCALvListResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_apireff_rcalev1")
    @Expose
    val cvApireffRca: List<CvApireffRcalev1>? = null

    @SerializedName("cv_apireff_rcalev2")
    @Expose
    val cvApireffRcalev2: List<CvApireffRcalev2>? = null

    @SerializedName("cv_apireff_rcalev3")
    @Expose
    val cvApireffRcalev3: List<CvApireffRcalev3>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvApireffRcalev1 {
        @SerializedName("rca_lev_1_ID")
        @Expose
        var rcaLev1ID: String? = null

        @SerializedName("rca_lev_1")
        @Expose
        var rcaLev1: String? = null
    }

    class CvApireffRcalev2 {
        @SerializedName("rca_lev_2_ID")
        @Expose
        var rcaLev2ID: String? = null

        @SerializedName("rca_lev_1_ID")
        @Expose
        var rcaLev1ID: String? = null

        @SerializedName("rca_lev_2")
        @Expose
        var rcaLev2: String? = null
    }

    class CvApireffRcalev3 {
        @SerializedName("rca_lev_3_ID")
        @Expose
        var rcaLev3ID: String? = null

        @SerializedName("rca_lev_2_ID")
        @Expose
        var rcaLev2ID: String? = null

        @SerializedName("rca_lev_3")
        @Expose
        var rcaLev3: String? = null
    }
}