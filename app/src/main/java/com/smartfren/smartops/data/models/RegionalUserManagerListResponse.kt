package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegionalUserManagerListResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_regusermgm")
    @Expose
    val cvRegusermgm: List<CvRegusermgm>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvRegusermgm {
        @SerializedName("id_user")
        @Expose
        var idUser: String? = null

        @SerializedName("u_company")
        @Expose
        var uCompany: String? = null

        @SerializedName("u_region")
        @Expose
        var uRegion: String? = null

        @SerializedName("id_parrent")
        @Expose
        var idParrent: String? = null

        @SerializedName("u_name")
        @Expose
        var uName: String? = null

        @SerializedName("u_level")
        @Expose
        var uLevel: String? = null

        @SerializedName("u_activation")
        @Expose
        var uActivation: String? = null

        @SerializedName("u_tagging_status")
        @Expose
        var uTaggingStatus: Any? = null
    }
}