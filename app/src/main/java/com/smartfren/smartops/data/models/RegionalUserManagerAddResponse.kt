package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegionalUserManagerAddResponse {
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

        @SerializedName("u_region")
        @Expose
        var uRegion: Int? = null

        @SerializedName("id_parrent")
        @Expose
        var idParrent: String? = null

        @SerializedName("username")
        @Expose
        var username: String? = null

        @SerializedName("password")
        @Expose
        var password: String? = null

        @SerializedName("u_name")
        @Expose
        var uName: String? = null

        @SerializedName("u_phone")
        @Expose
        var uPhone: String? = null

        @SerializedName("u_level")
        @Expose
        var uLevel: Int? = null

        @SerializedName("u_mail")
        @Expose
        var uMail: String? = null

        @SerializedName("u_activation")
        @Expose
        var uActivation: String? = null

        @SerializedName("u_registrars")
        @Expose
        var uRegistrars: Int? = null

        @SerializedName("u_registrationdate")
        @Expose
        var uRegistrationdate: String? = null
    }
}