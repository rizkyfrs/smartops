package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class SiteInfoResponse {
    @SerializedName("report")
    @Expose
    private var report: Report? = null

    fun getReport(): Report? {
        return report
    }

    fun setReport(report: Report?) {
        this.report = report
    }

    class Info {
        @SerializedName("siteID")
        @Expose
        var siteID: String? = null

        @SerializedName("siteOldID")
        @Expose
        var siteOldID: String? = null

        @SerializedName("siteIDFAP")
        @Expose
        var siteIDFAP: String? = null

        @SerializedName("siteIDCDMA")
        @Expose
        var siteIDCDMA: String? = null

        @SerializedName("OMRegionName")
        @Expose
        var oMRegionName: String? = null

        @SerializedName("OMManager")
        @Expose
        var oMManager: String? = null

        @SerializedName("OMAreaCoord")
        @Expose
        var oMAreaCoord: String? = null

        @SerializedName("OMClusterPIC")
        @Expose
        var oMClusterPIC: String? = null

        @SerializedName("siteName")
        @Expose
        var siteName: String? = null

        @SerializedName("siteCity")
        @Expose
        var siteCity: String? = null

        @SerializedName("siteStatus")
        @Expose
        var siteStatus: String? = null

        @SerializedName("siteLongitude")
        @Expose
        var siteLongitude: String? = null

        @SerializedName("siteLatitude")
        @Expose
        var siteLatitude: String? = null

        @SerializedName("lastUpdatedBy")
        @Expose
        var lastUpdatedBy: Any? = null

        @SerializedName("lastUpdatedOn")
        @Expose
        var lastUpdatedOn: Any? = null

        @SerializedName("createdBy")
        @Expose
        var createdBy: Any? = null

        @SerializedName("createdOn")
        @Expose
        var createdOn: String? = null

        @SerializedName("siteCostCenter")
        @Expose
        var siteCostCenter: String? = null

        @SerializedName("towerProviderGroup")
        @Expose
        var towerProviderGroup: String? = null

        @SerializedName("towerProvider")
        @Expose
        var towerProvider: String? = null

        @SerializedName("siteSOArea")
        @Expose
        var siteSOArea: String? = null

        @SerializedName("siteTPCategory")
        @Expose
        var siteTPCategory: String? = null

        @SerializedName("siteTPRemarks")
        @Expose
        var siteTPRemarks: Any? = null

        @SerializedName("siteBudgetCategory")
        @Expose
        var siteBudgetCategory: String? = null

        @SerializedName("siteTechPlan")
        @Expose
        var siteTechPlan: String? = null

        @SerializedName("siteCoverageType")
        @Expose
        var siteCoverageType: String? = null

        @SerializedName("siteContractType")
        @Expose
        var siteContractType: String? = null

        @SerializedName("siteMaintenanceClass")
        @Expose
        var siteMaintenanceClass: String? = null

        @SerializedName("siteManagedServiceCat")
        @Expose
        var siteManagedServiceCat: String? = null

        @SerializedName("leasePricePerYear")
        @Expose
        var leasePricePerYear: Any? = null

        @SerializedName("groundLeaseExpire")
        @Expose
        var groundLeaseExpire: Any? = null

        @SerializedName("managedServiceRemarks")
        @Expose
        var managedServiceRemarks: Any? = null

        @SerializedName("dateLeaseStart")
        @Expose
        var dateLeaseStart: String? = null

        @SerializedName("dateLeaseEnd")
        @Expose
        var dateLeaseEnd: String? = null

        @SerializedName("dateMOS")
        @Expose
        var dateMOS: String? = null

        @SerializedName("dateLTERFI")
        @Expose
        var dateLTERFI: String? = null

        @SerializedName("dateIntegration")
        @Expose
        var dateIntegration: String? = null

        @SerializedName("dateOnAir")
        @Expose
        var dateOnAir: String? = null

        @SerializedName("dateOffAir")
        @Expose
        var dateOffAir: Any? = null

        @SerializedName("dateMaterialDismantling")
        @Expose
        var dateMaterialDismantling: Any? = null

        @SerializedName("dateMaterialReturn")
        @Expose
        var dateMaterialReturn: Any? = null

        @SerializedName("statusValidity")
        @Expose
        var statusValidity: Any? = null

        @SerializedName("statusValidityRemarks")
        @Expose
        var statusValidityRemarks: Any? = null

        @SerializedName("PLNID")
        @Expose
        var plnid: Any? = null

        @SerializedName("PLNTariffGroup")
        @Expose
        var pLNTariffGroup: String? = null

        @SerializedName("PLNCapacity")
        @Expose
        var pLNCapacity: Any? = null

        @SerializedName("PLNPayment")
        @Expose
        var pLNPayment: String? = null

        @SerializedName("PLNPaymentPoint")
        @Expose
        var pLNPaymentPoint: String? = null

        @SerializedName("SiteFixedGenset")
        @Expose
        var siteFixedGenset: String? = null

        @SerializedName("SiteFixedGensetType")
        @Expose
        var siteFixedGensetType: Any? = null

        @SerializedName("siteFixedGensetCapacity")
        @Expose
        var siteFixedGensetCapacity: Any? = null

        @SerializedName("siteFixedGensetStatus")
        @Expose
        var siteFixedGensetStatus: Any? = null

        @SerializedName("siteFixedGensetRepairTarget")
        @Expose
        var siteFixedGensetRepairTarget: Any? = null

        @SerializedName("siteFixedGensetNOKRemarks")
        @Expose
        var siteFixedGensetNOKRemarks: Any? = null

        @SerializedName("siteFixedGensetFuel")
        @Expose
        var siteFixedGensetFuel: Any? = null

        @SerializedName("siteFixedGensetPMDate")
        @Expose
        var siteFixedGensetPMDate: Any? = null

        @SerializedName("siteFixedGensetFuelAlarm")
        @Expose
        var siteFixedGensetFuelAlarm: Any? = null

        @SerializedName("siteFixedGensetWarmupSensor")
        @Expose
        var siteFixedGensetWarmupSensor: Any? = null

        @SerializedName("siteMorphology")
        @Expose
        var siteMorphology: String? = null

        @SerializedName("siteTotalCell")
        @Expose
        var siteTotalCell: String? = null

        @SerializedName("siteUplinkID")
        @Expose
        var siteUplinkID: String? = null

        @SerializedName("siteDownlinkCount")
        @Expose
        var siteDownlinkCount: String? = null

        @SerializedName("siteLocationType")
        @Expose
        var siteLocationType: String? = null

        @SerializedName("siteDependency")
        @Expose
        var siteDependency: String? = null

        @SerializedName("siteFONode")
        @Expose
        var siteFONode: String? = null

        @SerializedName("siteIPTXStatus")
        @Expose
        var siteIPTXStatus: String? = null

        @SerializedName("siteTowerType")
        @Expose
        var siteTowerType: String? = null

        @SerializedName("siteTowerHeight")
        @Expose
        var siteTowerHeight: String? = null

        @SerializedName("siteBuildingHeight")
        @Expose
        var siteBuildingHeight: Any? = null

        @SerializedName("siteTotalHeight")
        @Expose
        var siteTotalHeight: Any? = null

        @SerializedName("siteAntennaNum")
        @Expose
        var siteAntennaNum: Any? = null

        @SerializedName("siteAntennaType")
        @Expose
        var siteAntennaType: Any? = null

        @SerializedName("siteTXType")
        @Expose
        var siteTXType: String? = null

        @SerializedName("siteRFAntennaQTY")
        @Expose
        var siteRFAntennaQTY: Any? = null

        @SerializedName("siteRRUQTY")
        @Expose
        var siteRRUQTY: Any? = null

        @SerializedName("siteMWAntennaQTY")
        @Expose
        var siteMWAntennaQTY: Any? = null

        @SerializedName("siteMWAntennaDiameter")
        @Expose
        var siteMWAntennaDiameter: Any? = null
    }

    class Report {
        @SerializedName("success")
        @Expose
        var success: Boolean? = null

        @SerializedName("message")
        @Expose
        var message: String? = null

        @SerializedName("info")
        @Expose
        var info: List<Info>? = null

        @SerializedName("token_status")
        @Expose
        var tokenStatus: String? = null
    }

}