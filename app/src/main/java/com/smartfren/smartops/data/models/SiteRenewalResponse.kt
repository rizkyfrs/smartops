package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteRenewalResponse {
    @SerializedName("report")
    @Expose
    var report: Report? = null

    class Info {
        @SerializedName("id_region")
        @Expose
        var idRegion: String? = null

        @SerializedName("region_name")
        @Expose
        var regionName: String? = null

        @SerializedName("lease_due")
        @Expose
        var leaseDue: String? = null

        @SerializedName("VALIDATION")
        @Expose
        var validation: String? = null

        @SerializedName("PRICENEGO")
        @Expose
        var pricenego: String? = null

        @SerializedName("SITARA")
        @Expose
        var sitara: String? = null

        @SerializedName("PKS")
        @Expose
        var pks: String? = null

        @SerializedName("PAYMENT")
        @Expose
        var payment: String? = null

        @SerializedName("RENEWAL")
        @Expose
        var renewal: String? = null

        @SerializedName("DROP")
        @Expose
        var drop: String? = null

        @SerializedName("RELOC")
        @Expose
        var reloc: String? = null

        @SerializedName("TSUM")
        @Expose
        var tsum: String? = null
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