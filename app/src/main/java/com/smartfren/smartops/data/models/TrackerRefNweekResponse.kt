package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerRefNweekResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("ref_n_week")
    @Expose
    var refNWeek: List<RefNWeek>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class RefNWeek {
        @SerializedName("value")
        @Expose
        var value: String? = null

        @SerializedName("text")
        @Expose
        var text: String? = null
    }
}