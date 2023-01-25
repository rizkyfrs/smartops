package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class LoginResponse {
    @SerializedName("report")
    @Expose
    var report: Report? = null
}

class Report {
    @SerializedName("authorized")
    @Expose
    var authorized: Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("user_attributes")
    @Expose
    var userAttributes: List<UserAttribute>? = null
}

class UserAttribute {
    @SerializedName("id_user")
    @Expose
    var idUser: String? = null

    @SerializedName("u_name")
    @Expose
    private var uName: String? = null

    @SerializedName("u_level")
    @Expose
    private var uLevel: String? = null

    @SerializedName("u_region")
    @Expose
    private var uRegion: String? = null

    @SerializedName("region_name")
    @Expose
    var regionName: String? = null

    @SerializedName("u_token")
    @Expose
    private var uToken: String? = null
    fun getuName(): String? {
        return uName
    }

    fun setuName(uName: String?) {
        this.uName = uName
    }

    fun getuLevel(): String? {
        return uLevel
    }

    fun setuLevel(uLevel: String?) {
        this.uLevel = uLevel
    }

    fun getuRegion(): String? {
        return uRegion
    }

    fun setuRegion(uRegion: String?) {
        this.uRegion = uRegion
    }

    fun getuToken(): String? {
        return uToken
    }

    fun setuToken(uToken: String?) {
        this.uToken = uToken
    }
}