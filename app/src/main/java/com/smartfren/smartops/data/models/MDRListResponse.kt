package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MDRListResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("tb_stock_mdr")
    @Expose
    var tbStockMdr: List<TbStockMdr>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class TbStockMdr {
        @SerializedName("fsmdr_num")
        @Expose
        var fsmdrNum: String? = null

        @SerializedName("fsmdr_dop")
        @Expose
        var fsmdrDop: String? = null

        @SerializedName("fsmdr_id")
        @Expose
        var fsmdrId: String? = null

        @SerializedName("fsmdr_vendor")
        @Expose
        var fsmdrVendor: String? = null

        @SerializedName("fsmdr_wo_id")
        @Expose
        var fsmdrWoId: String? = null

        @SerializedName("fsmdr_site_id")
        @Expose
        var fsmdrSiteId: String? = null

        @SerializedName("fsmdr_date")
        @Expose
        var fsmdrDate: String? = null

        @SerializedName("fsmdr_status")
        @Expose
        var fsmdrStatus: String? = null

        @SerializedName("fsmdr_registrars")
        @Expose
        var fsmdrRegistrars: String? = null

        @SerializedName("fsmdr_registerdate")
        @Expose
        var fsmdrRegisterdate: String? = null
    }

}
