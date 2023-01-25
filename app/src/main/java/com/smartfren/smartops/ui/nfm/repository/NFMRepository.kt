package com.smartfren.smartops.ui.nfm.repository

import com.smartfren.smartops.data.models.NFMAlarmResponse
import com.smartfren.smartops.data.models.NFMIoTResponse
import com.smartfren.smartops.data.models.NFMSNMPResponse
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class NFMRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getListNFM(iot_id: String?): Response<NFMIoTResponse> {
        return apiService.postNFMIoT(iot_id)
    }

    suspend fun getDetailNFM(iot_id: String?, ne_id: String?): Response<NFMSNMPResponse> {
        return apiService.postNFMsnmp(iot_id, ne_id)
    }

    suspend fun getAlarmNFM(): Response<NFMAlarmResponse> {
        return apiService.getNFMAlarm()
    }
}