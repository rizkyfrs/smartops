package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegionalUserManagerViewResponse {
    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("cv_regusermgm")
    @Expose
    val cvRegusermgm: CvRegusermgm? = null

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

        @SerializedName("u_nik")
        @Expose
        var uNik: String? = null

        @SerializedName("regusername")
        @Expose
        var username: String? = null

        @SerializedName("regpassword")
        @Expose
        var password: String? = null

        @SerializedName("u_picture")
        @Expose
        var uPicture: String? = null

        @SerializedName("u_name")
        @Expose
        var uName: String? = null

        @SerializedName("u_phone")
        @Expose
        var uPhone: String? = null

        @SerializedName("u_level")
        @Expose
        var uLevel: String? = null

        @SerializedName("u_mail")
        @Expose
        var uMail: String? = null

        @SerializedName("u_activation")
        @Expose
        var uActivation: String? = null

        @SerializedName("u_registrars")
        @Expose
        var uRegistrars: String? = null

        @SerializedName("u_registrationdate")
        @Expose
        var uRegistrationdate: String? = null

        @SerializedName("u_tagging_status")
        @Expose
        var uTaggingStatus: String? = null

        @SerializedName("u_tagging_date")
        @Expose
        var uTaggingDate: String? = null
    }
}