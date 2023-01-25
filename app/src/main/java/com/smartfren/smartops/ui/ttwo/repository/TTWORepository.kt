package com.smartfren.smartops.ui.ttwo.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class TTWORepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListTTWO(
        start: String?,
        recperpage: String?
    ): Response<TTWOListResponse> {
        return apiService.getTTWOList(start, recperpage)
    }

    suspend fun getViewTTWO(key: String?, ): Response<TTWOViewResponse> {
        return apiService.getTTWOView(key)
    }

    suspend fun getListTTWORCA(
        key: String?,
        recperpage: String?,
        rca1: String?,
        rca2: String?
    ): Response<TTWORCALvListResponse> {
        return apiService.getTTWORCAList(key, recperpage, rca1, rca2)
    }

    suspend fun postUpdateTTWORCA(
        key: String?, u_rca_1: String?, u_rca_2: String?, u_rca_3: String?,
        time_duration: String?, site_pic_id: String?, site_id: String?
    ): Response<TTWORCAUpdateResponse> {
        return apiService.postUpdateTTWORCA(
            key, u_rca_1, u_rca_2, u_rca_3, time_duration, site_pic_id, site_id
        )
    }
}