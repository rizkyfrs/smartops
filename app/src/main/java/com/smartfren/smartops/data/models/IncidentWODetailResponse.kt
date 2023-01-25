package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class IncidentWODetailResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("tb_incident_workorder")
    @Expose
    var tbIncidentDetailWorkorder: TbIncidentWorkorder? = null

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
        val incwoId: String? = null

        @SerializedName("incwo_srs_tt")
        @Expose
        val incwoSrsTt: String? = null

        @SerializedName("incwo_woid")
        @Expose
        val incwoWoid: String? = null

        @SerializedName("incwo_starttime")
        @Expose
        val incwoStarttime: String? = null

        @SerializedName("incwo_endtime")
        @Expose
        val incwoEndtime: String? = null

        @SerializedName("incwo_duration")
        @Expose
        val incwoDuration: String? = null

        @SerializedName("incwo_status")
        @Expose
        val incwoStatus: String? = null

        @SerializedName("incwo_rca1")
        @Expose
        val incwoRca1: String? = null

        @SerializedName("incwo_rca2")
        @Expose
        val incwoRca2: String? = null

        @SerializedName("incwo_rca3")
        @Expose
        val incwoRca3: String? = null

        @SerializedName("incwo_rca_detail")
        @Expose
        val incwoRcaDetail: String? = null

        @SerializedName("incwo_info")
        @Expose
        val incwoInfo: String? = null

        @SerializedName("incwo_pic_team")
        @Expose
        val incwoPicTeam: String? = null

        @SerializedName("incwo_accept_time")
        @Expose
        val incwoAcceptTime: String? = null

        @SerializedName("incwo_accept_by")
        @Expose
        val incwoAcceptBy: String? = null

        @SerializedName("incwo_ack_time")
        @Expose
        val incwoAckTime: String? = null

        @SerializedName("incwo_ack_by")
        @Expose
        val incwoAckBy: String? = null

        @SerializedName("incwo_departure_time")
        @Expose
        val incwoDepartureTime: String? = null

        @SerializedName("incwo_departure_by")
        @Expose
        val incwoDepartureBy: String? = null

        @SerializedName("incwo_arrive_time")
        @Expose
        val incwoArriveTime: String? = null

        @SerializedName("incwo_arrive_by")
        @Expose
        val incwoArriveBy: String? = null

        @SerializedName("incwo_troubleshoot_time")
        @Expose
        val incwoTroubleshootTime: String? = null

        @SerializedName("incwo_troubleshoot_by")
        @Expose
        val incwoTroubleshootBy: String? = null

        @SerializedName("incwo_troubleshoot_action")
        @Expose
        val incwoTroubleshootAction: String? = null

        @SerializedName("incwo_resolved_time")
        @Expose
        val incwoResolvedTime: String? = null

        @SerializedName("incwo_resolved_by")
        @Expose
        val incwoResolvedBy: String? = null

        @SerializedName("incwo_suggestion")
        @Expose
        val incwoSuggestion: String? = null

        @SerializedName("incwo_updated_time")
        @Expose
        val incwoUpdatedTime: String? = null

        @SerializedName("incwo_category")
        @Expose
        val incwoCategory: String? = null

        @SerializedName("incwo_sub_team")
        @Expose
        val incwoSubTeam: String? = null

        @SerializedName("incwo_region")
        @Expose
        val incwoRegion: String? = null

        @SerializedName("incwo_onsite_coordinate")
        @Expose
        val incwoOnsiteCoordinate: String? = null

        @SerializedName("incwo_action")
        @Expose
        val incwoAction: String? = null

        @SerializedName("inc_site_id")
        @Expose
        val incSiteId: String? = null

        @SerializedName("inc_site_name")
        @Expose
        val incSiteName: String? = null

        @SerializedName("inc_occurence_time")
        @Expose
        val incOccurenceTime: String? = null
    }
}