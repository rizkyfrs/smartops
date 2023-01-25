package com.smartfren.smartops.ui.sitemanagementinfo.repository

import com.smartfren.smartops.data.models.SiteManagementInfoDetailResponse
import com.smartfren.smartops.data.models.SiteManagementInfoResponse
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class SiteManagementInfoRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getSiteManagementInfo(start: String?, recperpage: String?): Response<SiteManagementInfoResponse> {
        return apiService.getSiteManagementInfo(start, recperpage)
    }

    suspend fun getSiteManagementInfoDetail(key: String?): Response<SiteManagementInfoDetailResponse> {
        return apiService.getSiteManagementInfoDetail(key)
    }
}