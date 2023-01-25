package com.smartfren.smartops.ui.workordermanagement.repository

import com.smartfren.smartops.data.models.IncidentWODetailResponse
import com.smartfren.smartops.data.models.IncidentWOListResponse
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class WorkorderRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getListWorkOrder(
        status: String?,
        start: String?,
        recperpage: String?
    ): Response<IncidentWOListResponse> {
        return apiService.getListIncidentWO(status, start, recperpage)
    }

    suspend fun getViewWorkOrder(key: String?): Response<IncidentWODetailResponse> {
        return apiService.getDetailIncidentWO(key)
    }

    suspend fun postEditWorkOrder(
        key: String?, incwo_status: String?, incwo_departure_time: String?, incwo_departure_by: String?, incwo_arrive_time: String?,
        incwo_arrive_by: String?, incwo_onsite_coordinate: String?, incwo_troubleshoot_time: String?, incwo_troubleshoot_by: String?,
        incwo_troubleshoot_action: String?, incwo_rca1: String?, incwo_rca2: String?, incwo_rca3: String?, incwo_action: String?, incwo_pic_team: String?
    ): Response<IncidentWODetailResponse> {
        return apiService.postEditIncidentWO(
            key,
            incwo_status,
            incwo_departure_time,
            incwo_departure_by,
            incwo_arrive_time,
            incwo_arrive_by,
            incwo_onsite_coordinate,
            incwo_troubleshoot_time,
            incwo_troubleshoot_by,
            incwo_troubleshoot_action,
            incwo_rca1,
            incwo_rca2,
            incwo_rca3,
            "",
            incwo_action,
            incwo_pic_team
        )
    }
}