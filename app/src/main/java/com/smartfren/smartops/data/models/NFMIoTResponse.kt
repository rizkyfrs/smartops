package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NFMIoTResponse {
    @SerializedName("ne")
    @Expose
    var ne: List<Ne?>? = null

    @SerializedName("snmp_dev")
    @Expose
    var snmpDev: List<SnmpDev?>? = null

    class Ne {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("iot")
        @Expose
        var iot: String? = null

        @SerializedName("relay_id")
        @Expose
        var relayId: String? = null

        @SerializedName("relay_name")
        @Expose
        var relayName: String? = null

        @SerializedName("ne1")
        @Expose
        var ne1: String? = null

        @SerializedName("ne2")
        @Expose
        var ne2: String? = null

        @SerializedName("ne3")
        @Expose
        var ne3: String? = null

        @SerializedName("ne4")
        @Expose
        var ne4: String? = null

        @SerializedName("ne")
        @Expose
        var ne: String? = null

        @SerializedName("relay_mode")
        @Expose
        var relayMode: String? = null

        @SerializedName("relay_value")
        @Expose
        var relayValue: String? = null
    }

    class SnmpDev {
        @SerializedName("id")
        @Expose
        var id: String? = null

        @SerializedName("ne")
        @Expose
        var ne: String? = null

        @SerializedName("ip")
        @Expose
        var ip: String? = null

        @SerializedName("status")
        @Expose
        var status: String? = null

        @SerializedName("time")
        @Expose
        var time: String? = null
    }
}