package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class STOMMilestoneDLResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("crs_stom_milestone_dl")
    @Expose
    var crsStomMilestoneDl: List<CrsStomMilestoneDl>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class CrsStomMilestoneDl {
        @SerializedName("led_group")
        @Expose
        var ledGroup: String? = null

        @SerializedName("jml_site")
        @Expose
        var jmlSite: Int? = null

        @SerializedName("dismantle_drop")
        @Expose
        var dismantleDrop: Int? = null

        @SerializedName("renewal_done")
        @Expose
        var renewalDone: Int? = null

        @SerializedName("payment_process")
        @Expose
        var paymentProcess: Int? = null

        @SerializedName("pks_process")
        @Expose
        var pksProcess: Int? = null

        @SerializedName("sitara_process")
        @Expose
        var sitaraProcess: Int? = null

        @SerializedName("pricing_nego")
        @Expose
        var pricingNego: Int? = null

        @SerializedName("review_process")
        @Expose
        var reviewProcess: Int? = null

        @SerializedName("not_yet_submit")
        @Expose
        var notYetSubmit: Int? = null
    }
}