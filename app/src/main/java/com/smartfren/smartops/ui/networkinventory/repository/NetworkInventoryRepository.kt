package com.smartfren.smartops.ui.networkinventory.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class NetworkInventoryRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getSiteInfoBySite(): Response<SiteInfoBySiteSumResponse> {
        return apiService.getSiteInfoBySite()
    }

    suspend fun getSiteInfoByVendor(): Response<SiteInfoByVendorSumResponse> {
        return apiService.getSiteInfoByVendor()
    }

    suspend fun getSiteInfoByByFONode(): Response<SiteInfoByFONodeSumResponse> {
        return apiService.getSiteInfoByFONode()
    }

    suspend fun getSiteInfoByModel(): Response<SiteInfoBySiteModelSumResponse> {
        return apiService.getSiteInfoByModel()
    }

    suspend fun getSiteInfoByFGS(): Response<SiteInfoByFGSSumResponse> {
        return apiService.getSiteInfoByFGS()
    }

    suspend fun getSiteInfoByTopology(): Response<SiteInfoBySiteTopologySumResponse> {
        return apiService.getSiteInfoByTopology()
    }

    suspend fun getClusterReport(token: String?): Response<ClusteringReportResponse> {
        return apiService.getClusteringRepost(token)
    }

    suspend fun getTeamAvailabilityReport(token: String?, id: String?): Response<TeamAvailabilityResponse> {
        return apiService.getTeamAvailability(token, id)
    }

    suspend fun getSmartopsLoginReport(token: String?, id: String?): Response<SmartopsLoginReportResponse> {
        return apiService.getSmartopsLogin(token, id)
    }

    suspend fun getMDMStatusFLMReport(token: String?, id: String?): Response<MDMStatusFLMResponse> {
        return apiService.getMDMStatusFLM(token, id)
    }
}