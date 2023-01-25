package com.smartfren.smartops.ui.ccf.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class CCFRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCCFDaily(): Response<CCFDailyResponse> {
        return apiService.getCCFD()
    }

    suspend fun getCCFWeekly(): Response<CCFWeeklyResponse> {
        return apiService.getCCFW()
    }

    suspend fun getCCFMonthly(): Response<CCFMonthlyResponse> {
        return apiService.getCCFM()
    }

    suspend fun getCCFNMonthly(): Response<CCFNMonthlyResponse> {
        return apiService.getCCFNM()
    }

    suspend fun getCCFYearly(): Response<CCFYearlyResponse> {
        return apiService.getCCFY()
    }

}