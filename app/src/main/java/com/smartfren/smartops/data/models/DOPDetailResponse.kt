package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DOPDetailResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("tb_dop")
    @Expose
    var tbDop: TbDop? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class TbDop {
        @SerializedName("dop_num")
        @Expose
        var dopNum: String? = null

        @SerializedName("dop_region")
        @Expose
        var dopRegion: String? = null

        @SerializedName("dop_city")
        @Expose
        var dopCity: String? = null

        @SerializedName("dop_status")
        @Expose
        var dopStatus: String? = null

        @SerializedName("dop_address")
        @Expose
        var dopAddress: String? = null

        @SerializedName("dop_pic")
        @Expose
        var dopPic: String? = null

        @SerializedName("dop_pic_number")
        @Expose
        var dopPicNumber: String? = null

        @SerializedName("dop_pic_ah")
        @Expose
        var dopPicAh: String? = null

        @SerializedName("dop_pic_number_ah")
        @Expose
        var dopPicNumberAh: String? = null

        @SerializedName("registrars")
        @Expose
        var registrars: Any? = null

        @SerializedName("registerdate")
        @Expose
        var registerdate: Any? = null
    }
}