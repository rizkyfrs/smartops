package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TTWORCAUpdateResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_fsttwohistRCA")
    @Expose
    val cvFsttwohistRCA: CvFsttwohistRCA? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvFsttwohistRCA {
        @SerializedName("u_rca_1")
        @Expose
        var uRca1: String? = null

        @SerializedName("u_rca_2")
        @Expose
        var uRca2: String? = null

        @SerializedName("u_rca_3")
        @Expose
        var uRca3: String? = null
    }
}