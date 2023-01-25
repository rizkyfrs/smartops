package com.smartfren.smartops.ui.history.repository

import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.data.models.ActivityHistoryLogResponse
import retrofit2.Response
import javax.inject.Inject

class MyHistoryRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListHistory(token: String?, page: String?): Response<ActivityHistoryLogResponse> {
        return apiService.getActivityHistoryLog(token, page)
    }
}