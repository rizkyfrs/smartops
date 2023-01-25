package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteManagementInfoResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("cv_hub_siteinfo")
    @Expose
    var cvHubSiteinfo: List<CvHubSiteinfo>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvHubSiteinfo {
        @SerializedName("site_id")
        @Expose
        var siteId: String? = null

        @SerializedName("site_vendor")
        @Expose
        var siteVendor: String? = null

        @SerializedName("site_tag")
        @Expose
        var siteTag: String? = null

        @SerializedName("site_status")
        @Expose
        var siteStatus: String? = null

        @SerializedName("site_sales_region")
        @Expose
        var siteSalesRegion: String? = null

        @SerializedName("site_technology")
        @Expose
        var siteTechnology: String? = null

        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("site_morphology")
        @Expose
        var siteMorphology: String? = null

        @SerializedName("site_loc_type")
        @Expose
        var siteLocType: String? = null

        @SerializedName("site_ipransw_status")
        @Expose
        var siteIpranswStatus: String? = null

        @SerializedName("tpm_tp_group")
        @Expose
        var tpmTpGroup: String? = null

        @SerializedName("tpm_site_ms_class")
        @Expose
        var tpmSiteMsClass: String? = null

        @SerializedName("ed_customer_id")
        @Expose
        var edCustomerId: String? = null

        @SerializedName("ed_cost_center")
        @Expose
        var edCostCenter: String? = null

        @SerializedName("pmStatus")
        @Expose
        var pmStatus: String? = null
    }
}