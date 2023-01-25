package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TrackerBatteryPDBMWResponse {
    @SerializedName("success")
    @Expose
    var success: String? = null

    @SerializedName("crs_pdb_mw_topology")
    @Expose
    var crsPdbMwTopology: List<CrsPdbMwTopology>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    class CrsPdbMwTopology {
        @SerializedName("mw_topology")
        @Expose
        var mwTopology: String? = null

        @SerializedName("rely_battery")
        @Expose
        var relyBattery: Int? = null

        @SerializedName("rely_gs")
        @Expose
        var relyGs: Int? = null

        @SerializedName("rely_battery_release_GS")
        @Expose
        var relyBatteryReleaseGS: Int? = null

        @SerializedName("rely_fgs_release_GS")
        @Expose
        var relyFgsReleaseGS: Int? = null
    }
}