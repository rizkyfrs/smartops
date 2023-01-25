package com.smartfren.smartops.ui.reportpm.repository

import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.data.models.ReportPMResponse
import com.smartfren.smartops.data.models.SiteInfoResponse
import retrofit2.Response
import javax.inject.Inject

class ReportPMRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getReportSite(token: String?, site: String?): Response<SiteInfoResponse> {
        return apiService.getSiteInfo(token, site)
    }

    suspend fun getReportPM(token: String?, site: String?, date: String?): Response<ReportPMResponse> {
        return apiService.getPMTPTBG(token, site, date)
    }
}