package com.smartfren.smartops.ui.webch.repository

import com.smartfren.smartops.data.models.WebCHAvailabilityResponse
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class WebCHAvailabilityRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getWebCHAvailability(token: String?, region: String?, period: String?): Response<WebCHAvailabilityResponse> {
        return apiService.getWebCHAvailability(token, region, period)
    }
}