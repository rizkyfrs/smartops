package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginResponseNew {
    @SerializedName("JWT")
    @Expose
    var jwt: String? = null
}