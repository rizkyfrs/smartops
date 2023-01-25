package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IncidentWOListResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("tb_incident_workorder")
    @Expose
    var tbIncidentWorkorder: List<TbIncidentWorkorder>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class TbIncidentWorkorder {
        @SerializedName("incwo_id")
        @Expose
        var incwoId: String? = null

        @SerializedName("incwo_srs_tt")
        @Expose
        var incwoSrsTt: String? = null

        @SerializedName("incwo_woid")
        @Expose
        var incwoWoid: String? = null

        @SerializedName("incwo_starttime")
        @Expose
        var incwoStarttime: String? = null

        @SerializedName("incwo_endtime")
        @Expose
        var incwoEndtime: String? = null

        @SerializedName("incwo_duration")
        @Expose
        var incwoDuration: String? = null

        @SerializedName("incwo_status")
        @Expose
        var incwoStatus: String? = null

        @SerializedName("incwo_rca1")
        @Expose
        var incwoRca1: String? = null

        @SerializedName("incwo_rca2")
        @Expose
        var incwoRca2: String? = null

        @SerializedName("incwo_rca_detail")
        @Expose
        var incwoRcaDetail: String? = null

        @SerializedName("incwo_pic_team")
        @Expose
        var incwoPicTeam: String? = null

        @SerializedName("incwo_category")
        @Expose
        var incwoCategory: String? = null

        @SerializedName("incwo_sub_team")
        @Expose
        var incwoSubTeam: String? = null

        @SerializedName("incwo_region")
        @Expose
        var incwoRegion: String? = null

        @SerializedName("inc_site_id")
        @Expose
        var incSiteId: String? = null
    }

}