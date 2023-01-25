package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegionalUserLevelResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_apiref_reguserlevel")
    @Expose
    val cvApirefReguserlevel: List<CvApirefReguserlevel>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvApirefReguserlevel {
        @SerializedName("userlevelid")
        @Expose
        var userlevelid: String? = null

        @SerializedName("userlevelname")
        @Expose
        var userlevelname: String? = null
    }
}