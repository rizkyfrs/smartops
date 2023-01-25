package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerACResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("v_site_air_cond_tracker")
    @Expose
    var vSiteAirCondTracker: List<VSiteAirCondTracker?>? = null

    @SerializedName("cv_rtrackersum_aircond")
    @Expose
    var cvSiteAirCondTracker: List<CvRtrackersumAircond?>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class VSiteAirCondTracker {
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

    class CvRtrackersumAircond {
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