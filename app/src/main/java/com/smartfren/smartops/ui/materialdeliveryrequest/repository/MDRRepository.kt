package com.smartfren.smartops.ui.materialdeliveryrequest.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class MDRRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListMdr(start: String?, recperpage: String?): Response<MDRListResponse> {
        return apiService.getListMDR(start, recperpage)
    }

    suspend fun getListVendor(start: String?, recperpage: String?): Response<VendorListResponse> {
        return apiService.getListVendor(start, recperpage)
    }

    suspend fun getListDOPRegion(region: String?): Response<DOPListResponse> {
        return apiService.getListDop(region)
    }

    suspend fun getDetailDOP(key: String?): Response<DOPDetailResponse> {
        return apiService.getDetailDop(key)
    }

    suspend fun postAddMDR(
        fsmdr_status: String?,
        fsmdr_id: String?,
        fsmdr_vendor: String?,
        fsmdr_region: String?,
        fsmdr_dop: String?,
        fsmdr_site_id: String?,
        fsmdr_wo_id: String?,
        fsmdr_wo_dom: String?,
        fsmdr_wo_flag: String?,
        fsmdr_date: String?,
        fsmdr_registrars: String?,
        fsmdr_registerdate: String?
    ): Response<AddStockMDRResponse> {
        return apiService.postAddStockMDR(
            fsmdr_status,
            fsmdr_id,
            fsmdr_vendor,
            fsmdr_region,
            fsmdr_dop,
            fsmdr_site_id,
            fsmdr_wo_id,
            fsmdr_wo_dom,
            fsmdr_wo_flag,
            fsmdr_date,
            fsmdr_registrars,
            fsmdr_registerdate
        )
    }
}