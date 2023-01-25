package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SiteInfoByFONodeSumResponse {

    @SerializedName("success")
    @Expose
    val success: Boolean? = null

    @SerializedName("mvrepsitefonode")
    @Expose
    val mvrepsitefonode: List<Mvrepsitefonode>? = null

    @SerializedName("totalRecordCount")
    @Expose
    val totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    val version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class Mvrepsitefonode {
        @SerializedName("regionCode")
        @Expose
        var regionCode: String? = null

        @SerializedName("regionName")
        @Expose
        var regionName: String? = null

        @SerializedName("FO Node - Chain")
        @Expose
        var fONodeChain: String? = null

        @SerializedName("FO Node - Ring")
        @Expose
        var fONodeRing: String? = null

        @SerializedName("FO MCP")
        @Expose
        var foMcp: String? = null

        @SerializedName("FO FTIF")
        @Expose
        var foFtif: String? = null

        @SerializedName("NA")
        @Expose
        var na: String? = null

        @SerializedName("totalCount")
        @Expose
        var totalCount: String? = null
    }
}