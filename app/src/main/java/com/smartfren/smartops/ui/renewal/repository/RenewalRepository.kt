package com.smartfren.smartops.ui.renewal.repository

import com.smartfren.smartops.data.models.SiteRenewalResponse
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class RenewalRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getSiteRenewal(token: String?, leasedue: String?): Response<SiteRenewalResponse> {
        return apiService.getSiteRenewal(token, leasedue)
    }
}