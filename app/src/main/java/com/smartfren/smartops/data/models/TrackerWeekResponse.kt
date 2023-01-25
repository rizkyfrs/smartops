package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerWeekResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("ref_week")
    @Expose
    var refWeek: List<String>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null
}