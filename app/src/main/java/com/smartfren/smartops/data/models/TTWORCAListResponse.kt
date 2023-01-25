package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TTWORCAListResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_apireff_rca")
    @Expose
    val cvApireffRca: List<CvApireffRca>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvApireffRca {
        @SerializedName("rca_num")
        @Expose
        var rcaNum: String? = null

        @SerializedName("rca_type")
        @Expose
        var rcaType: String? = null

        @SerializedName("rca_lev_1")
        @Expose
        var rcaLev1: String? = null

        @SerializedName("rca_lev_2")
        @Expose
        var rcaLev2: String? = null

        @SerializedName("rca_lev_3")
        @Expose
        var rcaLev3: String? = null
    }
}