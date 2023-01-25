package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class STOMLeaseAllOtherResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("crs_stom_lease_other")
    @Expose
    var crsStomLease: List<CrsStomLease>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class CrsStomLease {
        @SerializedName("lease_status")
        @Expose
        var leaseStatus: String? = null

        @SerializedName("jml_site")
        @Expose
        var jmlSite: Int? = null
    }
}