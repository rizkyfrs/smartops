package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class STOMMilestoneTPResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("crs_stom_milestone_tp")
    @Expose
    var crsStomMilestoneTp: List<CrsStomMilestoneTp>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class CrsStomMilestoneTp {
        @SerializedName("led_group")
        @Expose
        var ledGroup: String? = null

        @SerializedName("jml_site")
        @Expose
        var jmlSite: Int? = null

        @SerializedName("renewal_done")
        @Expose
        var renewalDone: Int? = null

        @SerializedName("terminate_drop")
        @Expose
        var terminateDrop: Int? = null

        @SerializedName("lease_expired_renewal_onprocess")
        @Expose
        var leaseExpiredRenewalOnprocess: Int? = null

        @SerializedName("lease_valid_renewal_onprocess")
        @Expose
        var leaseValidRenewalOnprocess: Int? = null

        @SerializedName("lease_expired_onprocess_review")
        @Expose
        var leaseExpiredOnprocessReview: Int? = null

        @SerializedName("lease_valid_onprocess_review")
        @Expose
        var leaseValidOnprocessReview: Int? = null
    }
}