package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class AddStockMDRResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("tb_stock_mdr")
    @Expose
    var tbStockMdr: TbStockMdr? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class TbStockMdr {
        @SerializedName("fsmdr_status")
        @Expose
        var fsmdrStatus: Int? = null

        @SerializedName("fsmdr_id")
        @Expose
        var fsmdrId: String? = null

        @SerializedName("fsmdr_vendor")
        @Expose
        var fsmdrVendor: Int? = null

        @SerializedName("fsmdr_region")
        @Expose
        var fsmdrRegion: Int? = null

        @SerializedName("fsmdr_dop")
        @Expose
        var fsmdrDop: Int? = null

        @SerializedName("fsmdr_site_id")
        @Expose
        var fsmdrSiteId: String? = null

        @SerializedName("fsmdr_wo_id")
        @Expose
        var fsmdrWoId: String? = null

        @SerializedName("fsmdr_wo_dom")
        @Expose
        var fsmdrWoDom: String? = null

        @SerializedName("fsmdr_wo_flag")
        @Expose
        var fsmdrWoFlag: String? = null

        @SerializedName("fsmdr_date")
        @Expose
        var fsmdrDate: String? = null

        @SerializedName("fsmdr_registrars")
        @Expose
        var fsmdrRegistrars: String? = null

        @SerializedName("fsmdr_registerdate")
        @Expose
        var fsmdrRegisterdate: String? = null

        @SerializedName("fsmdr_num")
        @Expose
        var fsmdrNum: String? = null
    }
}