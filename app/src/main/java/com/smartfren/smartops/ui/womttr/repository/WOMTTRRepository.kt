package com.smartfren.smartops.ui.womttr.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class WOMTTRRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getWOMTTR(): Response<WOMTTRResponse> {
        return apiService.getWOMTTR()
    }

}