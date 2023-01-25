package com.smartfren.smartops.ui.stom.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class STOMRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getSTOMLeaseAllTP(): Response<STOMLeaseAllTPResponse> {
        return apiService.getListSTOMLeaseTP()
    }

    suspend fun getSTOMLeaseDL(): Response<STOMLeaseAllDLResponse> {
        return apiService.getListSTOMLeaseDL()
    }

    suspend fun getSTOMLeaseIB(): Response<STOMLeaseAllIBResponse> {
        return apiService.getListSTOMLeaseIB()
    }

    suspend fun getSTOMLeaseOther(): Response<STOMLeaseAllOtherResponse> {
        return apiService.getListSTOMLeaseOther()
    }

    suspend fun getSTOMMilestoneTP(): Response<STOMMilestoneTPResponse> {
        return apiService.getListSTOMMilestoneTP()
    }

    suspend fun getSTOMMilestoneDL(): Response<STOMMilestoneDLResponse> {
        return apiService.getListSTOMMilestoneDL()
    }

    suspend fun getSTOMMilestoneIB(): Response<STOMMilestoneIBResponse> {
        return apiService.getListSTOMMilestoneIB()
    }
}