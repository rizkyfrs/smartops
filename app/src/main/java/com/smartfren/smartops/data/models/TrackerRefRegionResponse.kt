package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerRefRegionResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("ref_region")
    @Expose
    var refRegion: List<RefRegion>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class RefRegion {
        @SerializedName("value")
        @Expose
        var value: String? = null

        @SerializedName("text")
        @Expose
        var text: String? = null
    }
}