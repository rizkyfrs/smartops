package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class VendorListResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("tb_vendor")
    @Expose
    var tbVendor: List<TbVendor?>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class TbVendor {
        @SerializedName("id_vendor")
        @Expose
        var idVendor: String? = null

        @SerializedName("vendor_name")
        @Expose
        var vendorName: String? = null

        @SerializedName("vendor_desc")
        @Expose
        var vendorDesc: String? = null

        @SerializedName("vendor_type")
        @Expose
        var vendorType: String? = null

        @SerializedName("vendor_status")
        @Expose
        var vendorStatus: String? = null

        @SerializedName("vendor_registrars")
        @Expose
        var vendorRegistrars: String? = null

        @SerializedName("vendor_registerdate")
        @Expose
        var vendorRegisterdate: String? = null
    }
}