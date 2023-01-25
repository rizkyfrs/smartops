package com.smartfren.smartops.ui.dailyactivity.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.utils.convertStringToRequestBody
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class DailyActivityRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListSchedule(token: String?, sort: String?): Response<ScheduleResponse> {
        return apiService.scheduleList(token, sort)
    }

    suspend fun getTeamListSchedule(token: String?): Response<TeamListResponse> {
        return apiService.getTeamList(token)
    }

    suspend fun getDateListSchedule(token: String?): Response<WorkingDateResponse> {
        return apiService.getWorkingDate(token)
    }

    suspend fun getListDetailSchedule(token: String?, site: String?): Response<ScheduleTaskListDetailResponse> {
        return apiService.scheduleTaskListDetail(token, site)
    }

    suspend fun getActivityDetailSchedule(token: String?, site: String?, activityNumber: String?): Response<ScheduleTaskDetailResponse> {
        return apiService.scheduleTaskDetail(token, site, activityNumber)
    }

    suspend fun getSite(token: String?, site: String?): Response<ReferenceSiteResponse> {
        return apiService.getSites(token, site)
    }

    suspend fun getActLevel1(token: String?): Response<ReferenceDomainLev1Response> {
        return apiService.getActivityDomainLev1(token)
    }

    suspend fun getActLevel2(token: String?, domains: String?): Response<ReferenceDomainLev2Response> {
        return apiService.getActivityDomainLev2(token, domains)
    }

    suspend fun postValidateTask(token: String?, taskNum: String?): Response<GeneralResponse> {
        return apiService.postValidateTask(token, taskNum)
    }

    suspend fun postRCATask(token: String?, rca_level: String?, rca_group: String?, rca_parent: String?): Response<ListRCAResponse> {
        return apiService.postRCA(token, rca_level, rca_group, rca_parent)
    }

    suspend fun postUpdateTask(
        token: String?, site: String?, activityNumber: String?,
        pic_before: MultipartBody.Part?,
        pic_after: MultipartBody.Part?,
        rca_lev_1: String?,
        rca_lev_2: String?,
        rca_lev_3: String?,
        rca_action: String?,
        action_type: String?,
        action_detail: String?,
        activity_status: String?,
        pending_remarks_1: String?,
        pending_remarks_2: String?,
    ): Response<GeneralResponse> {
        return apiService.postUpdateTask(
            token, site, activityNumber, pic_before, pic_after,
            convertStringToRequestBody(rca_lev_1),
            convertStringToRequestBody(rca_lev_2),
            convertStringToRequestBody(rca_lev_3),
            convertStringToRequestBody(rca_action),
            convertStringToRequestBody(action_type),
            convertStringToRequestBody(action_detail),
            convertStringToRequestBody(activity_status),
            convertStringToRequestBody(pending_remarks_1),
            convertStringToRequestBody(pending_remarks_2)
        )
    }

    suspend fun postUpdateTaskManual(
        token: String?, site: String?,
        region_code: String?,
        domain_lev_1: String?,
        domain_lev_2: String?,
        site_id: String?,
        ne_name: String?,
        wo_id: String?,
        priority: String?,
        action_needed: String?,
        actual_date: String?,
        pic_before: MultipartBody.Part?,
        pic_after: MultipartBody.Part?,
        rca_lev_1: String?,
        rca_lev_2: String?,
        rca_lev_3: String?,
        rca_action: String?,
        action_type: String?,
        action_detail: String?,
        activity_status: String?,
        pending_remarks_1: String?,
        pending_remarks_2: String?,
        action_remark: String?,
    ): Response<GeneralResponse> {
        return apiService.postUpdateTaskManual(
            token, site,
            convertStringToRequestBody(region_code),
            convertStringToRequestBody(domain_lev_1),
            convertStringToRequestBody(domain_lev_2),
            convertStringToRequestBody(site_id),
            convertStringToRequestBody(ne_name),
            convertStringToRequestBody(wo_id),
            convertStringToRequestBody(priority),
            convertStringToRequestBody(action_needed),
            convertStringToRequestBody(actual_date),
            pic_before, pic_after,
            convertStringToRequestBody(rca_lev_1),
            convertStringToRequestBody(rca_lev_2),
            convertStringToRequestBody(rca_lev_3),
            convertStringToRequestBody(rca_action),
            convertStringToRequestBody(action_type),
            convertStringToRequestBody(action_detail),
            convertStringToRequestBody(activity_status),
            convertStringToRequestBody(pending_remarks_1),
            convertStringToRequestBody(pending_remarks_2),
            convertStringToRequestBody(action_remark)
        )
    }

    suspend fun getLiveFLMActivity(start: String?, recperpage:String?): Response<LiveFLMActivityListResponse> {
        return apiService.getListLiveFLMActivity(start, recperpage)
    }
}