package com.smartfren.smartops.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConsumableTypeResponse {
    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("cv_consumable_type")
    @Expose
    var cvConsumableType: List<CvConsumableType?>? = null

    @SerializedName("totalRecordCount")
    @Expose
    var totalRecordCount: Int? = null

    @SerializedName("version")
    @Expose
    var version: String? = null

    @SerializedName("failureMessage")
    @Expose
    var failureMessage: String? = null

    class CvConsumableType {
        @SerializedName("id_consumables")
        @Expose
        var idConsumables: String? = null

        @SerializedName("consumable_code")
        @Expose
        var consumableCode: String? = null

        @SerializedName("consumables_name")
        @Expose
        var consumablesName: String? = null

        @SerializedName("consumables_unit")
        @Expose
        var consumablesUnit: String? = null

        @SerializedName("consumables_registrars")
        @Expose
        var consumablesRegistrars: Any? = null

        @SerializedName("consumables_register_date")
        @Expose
        var consumablesRegisterDate: Any? = null
    }

}