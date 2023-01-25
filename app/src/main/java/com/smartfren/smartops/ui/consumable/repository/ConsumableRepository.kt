package com.smartfren.smartops.ui.consumable.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.utils.convertStringToRequestBody
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class ConsumableRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getConsumableUsageReport(
        start: String?,
        recperpage: String?
    ): Response<ConsumableUsageReportResponse> {
        return apiService.getConsumableUsageReport(start, recperpage)
    }

    suspend fun getConsumableUsageDetail(key: String?): Response<ConsumableUsageReportDetailResponse> {
        return apiService.getConsumableUsageDetail(key)
    }

    suspend fun getConsumableUsageType(): Response<ConsumableTypeResponse> {
        return apiService.getConsumableUsageType()
    }

    suspend fun postAddConsumableUsage(
        consType: String?,
        consSiteId: String?,
        consSiteVisitDate: String?,
        consNotes: String?,
        consUserId: String?,
        consRegisterDate: String?,
        consUsageBefore: MultipartBody.Part?,
        consUsageAfter: MultipartBody.Part?,
    ): Response<ConsumableAddReportResponse> {
        return apiService.postAddConsumableUsage(
            convertStringToRequestBody("2"), convertStringToRequestBody(consType),
            convertStringToRequestBody(consSiteId), convertStringToRequestBody(consSiteVisitDate),
            convertStringToRequestBody(consNotes), convertStringToRequestBody(consUserId),
            convertStringToRequestBody(consRegisterDate), consUsageBefore, consUsageAfter
        )
    }
}