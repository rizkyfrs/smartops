package com.smartfren.smartops.ui.sitevisit.repository

import com.smartfren.smartops.data.models.SiteVisitLogDetailResponse
import com.smartfren.smartops.data.models.SiteVisitLogRepose
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class SiteVisitRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getSiteVisitLog(start: String?, recperpage: String?): Response<SiteVisitLogRepose> {
        return apiService.getSiteVisitLog(start, recperpage)
    }

    suspend fun getSiteVisitLogSearch(key: String?): Response<SiteVisitLogDetailResponse> {
        return apiService.getSiteVisitLogSearch(key)
    }
}