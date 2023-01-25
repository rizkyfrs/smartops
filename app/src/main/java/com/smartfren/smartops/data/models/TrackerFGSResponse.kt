package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerFGSResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("v_site_fgs_tracker")
    @Expose
    var vSiteFgsTracker: List<VSiteFgsTracker?>? = null

    @SerializedName("cv_rtrackersum_fgenset")
    @Expose
    val cvRtrackersumFgenset: List<CvRtrackersumFgenset>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class VSiteFgsTracker {
        @SerializedName("region_nw")
        @Expose
        var regionNw: String? = null

        @SerializedName("total")
        @Expose
        var total: String? = null

        @SerializedName("nClose")
        @Expose
        var nClose: String? = null

        @SerializedName("nOpen")
        @Expose
        var nOpen: String? = null

        @SerializedName("nCancel")
        @Expose
        var nCancel: String? = null

        @SerializedName("super_hub")
        @Expose
        var superHub: String? = null

        @SerializedName("hub")
        @Expose
        var hub: String? = null

        @SerializedName("sub_hub")
        @Expose
        var subHub: String? = null

        @SerializedName("collector")
        @Expose
        var collector: String? = null

        @SerializedName("end_site")
        @Expose
        var endSite: String? = null
    }

    class CvRtrackersumFgenset {
        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("region_name")
        @Expose
        var regionName: String? = null

        @SerializedName("open")
        @Expose
        var open: String? = null

        @SerializedName("cancel")
        @Expose
        var cancel: String? = null

        @SerializedName("resolve")
        @Expose
        var resolve: String? = null

        @SerializedName("close")
        @Expose
        var close: String? = null

        @SerializedName("total")
        @Expose
        var total: String? = null

        @SerializedName("t.endsite")
        @Expose
        var tEndsite: String? = null

        @SerializedName("t.collector")
        @Expose
        var tCollector: String? = null

        @SerializedName("t.subhub")
        @Expose
        var tSubhub: String? = null

        @SerializedName("t.hub")
        @Expose
        var tHub: String? = null

        @SerializedName("t.superhub")
        @Expose
        var tSuperhub: String? = null
    }
}