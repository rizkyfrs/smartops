package com.smartfren.smartops.ui.corefacility.ccfreport.repository

import com.smartfren.smartops.data.models.CCFReportResponse
import com.smartfren.smartops.data.models.CFFReportDetailResponse
import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.data.models.GeneralResponse
import retrofit2.Response
import javax.inject.Inject

class CCFReportRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListCCFReport(token: String?, page: String?): Response<CCFReportResponse> {
        return apiService.getCCFActivityReport(token, page)
    }

    suspend fun getDetailCCFReport(token: String?, report_id: String?): Response<CFFReportDetailResponse> {
        return apiService.getCCFActivityReportDetail(token, report_id)
    }

    suspend fun postValidate(token: String?, activity_id: String?): Response<GeneralResponse> {
        return apiService.postValidateCCFReport(token, activity_id)
    }
}