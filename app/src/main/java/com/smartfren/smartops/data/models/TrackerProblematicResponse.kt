package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerProblematicResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("v_problematic_cluster")
    @Expose
    var vProblematicCluster: List<VProblematicCluster>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class VProblematicCluster {
        @SerializedName("urutan")
        @Expose
        var urutan: String? = null

        @SerializedName("region_nw")
        @Expose
        var regionNw: String? = null

        @SerializedName("ibs_scope")
        @Expose
        var ibsScope: String? = null

        @SerializedName("ibs_open")
        @Expose
        var ibsOpen: String? = null

        @SerializedName("ibs_close")
        @Expose
        var ibsClose: String? = null

        @SerializedName("non_ibs_scope")
        @Expose
        var nonIbsScope: String? = null

        @SerializedName("non_ibs_open")
        @Expose
        var nonIbsOpen: String? = null

        @SerializedName("non_ibs_close")
        @Expose
        var nonIbsClose: String? = null

        @SerializedName("total_scope")
        @Expose
        var totalScope: String? = null

        @SerializedName("total_open")
        @Expose
        var totalOpen: String? = null

        @SerializedName("total_close")
        @Expose
        var totalClose: String? = null
    }
}