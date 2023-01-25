package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteManagementInfoDetailResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("cv_hub_siteinfo")
    @Expose
    var cvHubSiteinfo: CvHubSiteinfo? = null

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

        @SerializedName("site_id_fap")
        @Expose
        var siteIdFap: String? = null

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

        @SerializedName("site_sales_cluster")
        @Expose
        var siteSalesCluster: String? = null

        @SerializedName("site_long")
        @Expose
        var siteLong: String? = null

        @SerializedName("site_lat")
        @Expose
        var siteLat: String? = null

        @SerializedName("site_address")
        @Expose
        var siteAddress: String? = null

        @SerializedName("site_address_propinsi")
        @Expose
        var siteAddressPropinsi: String? = null

        @SerializedName("site_address_kabupaten")
        @Expose
        var siteAddressKabupaten: String? = null

        @SerializedName("site_address_kecamatan")
        @Expose
        var siteAddressKecamatan: String? = null

        @SerializedName("site_address_kelurahan")
        @Expose
        var siteAddressKelurahan: String? = null

        @SerializedName("site_technology")
        @Expose
        var siteTechnology: String? = null

        @SerializedName("site_vvip_cat")
        @Expose
        var siteVvipCat: String? = null

        @SerializedName("site_phase")
        @Expose
        var sitePhase: String? = null

        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("site_cluster_name")
        @Expose
        var siteClusterName: String? = null

        @SerializedName("site_cluster_pic")
        @Expose
        var siteClusterPic: String? = null

        @SerializedName("site_area_coord")
        @Expose
        var siteAreaCoord: String? = null

        @SerializedName("site_manager")
        @Expose
        var siteManager: String? = null

        @SerializedName("site_fixgs")
        @Expose
        var siteFixgs: String? = null

        @SerializedName("site_gs_type")
        @Expose
        var siteGsType: String? = null

        @SerializedName("site_gs_cap")
        @Expose
        var siteGsCap: String? = null

        @SerializedName("site_morphology")
        @Expose
        var siteMorphology: String? = null

        @SerializedName("site_total_cell")
        @Expose
        var siteTotalCell: String? = null

        @SerializedName("site_uplink_id")
        @Expose
        var siteUplinkId: String? = null

        @SerializedName("site_loc_type")
        @Expose
        var siteLocType: String? = null

        @SerializedName("site_dependency")
        @Expose
        var siteDependency: String? = null

        @SerializedName("site_fo_node")
        @Expose
        var siteFoNode: String? = null

        @SerializedName("site_ipransw_status")
        @Expose
        var siteIpranswStatus: String? = null

        @SerializedName("site_tower_type")
        @Expose
        var siteTowerType: String? = null

        @SerializedName("site_tower_height")
        @Expose
        var siteTowerHeight: String? = null

        @SerializedName("site_building_height")
        @Expose
        var siteBuildingHeight: String? = null

        @SerializedName("site_total_height")
        @Expose
        var siteTotalHeight: String? = null

        @SerializedName("site_antenna_num")
        @Expose
        var siteAntennaNum: String? = null

        @SerializedName("site_antenna_type")
        @Expose
        var siteAntennaType: String? = null

        @SerializedName("site_tx_type")
        @Expose
        var siteTxType: String? = null

        @SerializedName("tpm_tp_group")
        @Expose
        var tpmTpGroup: String? = null

        @SerializedName("tpm_tp_name")
        @Expose
        var tpmTpName: String? = null

        @SerializedName("tpm_tp_category")
        @Expose
        var tpmTpCategory: String? = null

        @SerializedName("tpm_tp_remarks")
        @Expose
        var tpmTpRemarks: String? = null

        @SerializedName("tpm_site_so_area")
        @Expose
        var tpmSiteSoArea: String? = null

        @SerializedName("tpm_site_budget_cat")
        @Expose
        var tpmSiteBudgetCat: String? = null

        @SerializedName("tpm_site_tech_plan")
        @Expose
        var tpmSiteTechPlan: String? = null

        @SerializedName("tpm_site_coverage_type")
        @Expose
        var tpmSiteCoverageType: String? = null

        @SerializedName("tpm_site_contract_type")
        @Expose
        var tpmSiteContractType: String? = null

        @SerializedName("tpm_site_ms_class")
        @Expose
        var tpmSiteMsClass: String? = null

        @SerializedName("tpm_site_ms_vendor")
        @Expose
        var tpmSiteMsVendor: String? = null

        @SerializedName("tpm_site_lease_start")
        @Expose
        var tpmSiteLeaseStart: String? = null

        @SerializedName("tpm_site_lease_end")
        @Expose
        var tpmSiteLeaseEnd: String? = null

        @SerializedName("ed_customer_id")
        @Expose
        var edCustomerId: String? = null

        @SerializedName("ed_site_id")
        @Expose
        var edSiteId: String? = null

        @SerializedName("ed_site_region")
        @Expose
        var edSiteRegion: String? = null

        @SerializedName("ed_cost_center")
        @Expose
        var edCostCenter: String? = null

        @SerializedName("ed_company_code")
        @Expose
        var edCompanyCode: String? = null

        @SerializedName("ed_contract")
        @Expose
        var edContract: String? = null

        @SerializedName("ed_capacity")
        @Expose
        var edCapacity: String? = null

        @SerializedName("pmStatus")
        @Expose
        var pmStatus: String? = null

        @SerializedName("dDate")
        @Expose
        var dDate: String? = null

        @SerializedName("A1")
        @Expose
        var a1: String? = null

        @SerializedName("A2")
        @Expose
        var a2: String? = null

        @SerializedName("B")
        @Expose
        var b: String? = null

        @SerializedName("C")
        @Expose
        var c: String? = null

        @SerializedName("D")
        @Expose
        var d: String? = null

        @SerializedName("E")
        @Expose
        var e: String? = null

        @SerializedName("F")
        @Expose
        var f: String? = null
    }
}