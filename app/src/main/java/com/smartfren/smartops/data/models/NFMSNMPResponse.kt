package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NFMSNMPResponse {
    @SerializedName("snmp_val")
    @Expose
    var snmpVal: List<SnmpVal?>? = null

    class SnmpVal {
        @SerializedName("iot_ne_id")
        @Expose
        var iotNeId: String? = null

        @SerializedName("ip")
        @Expose
        var ip: String? = null

        @SerializedName("parameter")
        @Expose
        var parameter: String? = null

        @SerializedName("value")
        @Expose
        var value: String? = null

        @SerializedName("time")
        @Expose
        var time: String? = null

        @SerializedName("iot")
        @Expose
        var iot: String? = null

        @SerializedName("ne")
        @Expose
        var ne: String? = null
    }
}