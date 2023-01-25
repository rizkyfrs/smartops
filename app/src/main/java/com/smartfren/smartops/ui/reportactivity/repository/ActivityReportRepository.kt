package com.smartfren.smartops.ui.reportactivity.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.utils.convertStringToRequestBody
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Part
import retrofit2.http.Path
import javax.inject.Inject

class ActivityReportRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListActivityReport(token: String?, page: String?): Response<ActivityReportResponse> {
        return apiService.getActivityReport(token, page)
    }

    suspend fun getListActivityChecklist(token: String?, formNum: String?): Response<ActivityChecklistResponse> {
        return apiService.getActivityChecklist(token, formNum)
    }

    suspend fun getListFormGroup(token: String?): Response<ActivityReportGroupResponse> {
        return apiService.getActivityGroup(token)
    }

    suspend fun getListFormGroupType(token: String?, form_group: String?): Response<ActivityReportGroupTypeResponse> {
        return apiService.postActivityGroupType(token, form_group)
    }

    suspend fun getSite(token: String?, site: String?): Response<ReferenceSiteResponse> {
        return apiService.getSites(token, site)
    }

    suspend fun getListAdditionalChoice(token: String?, formNum: String?): Response<ChecklistItemAdditionalResponse> {
        return apiService.getChecklistItemAdditional(token, formNum)
    }

    suspend fun postActivityReportForm(
        token: String?,
        act_theme_id: String?,
        act_region: String?,
        act_site_id: String?,
        act_date: String?,
        act_avail: String?,
        act_notes: String?,
    ): Response<GeneralResponse> {
        return apiService.postActivityReportForm(token, act_theme_id, act_region, act_site_id, act_date, act_avail, act_notes)
    }

    suspend fun getChecklistID(token: String?, checklistID: String?): Response<ActivityChecklistIDResponse> {
        return apiService.getActivityChecklistID(token, checklistID)
    }

    suspend fun postActivityChecklistForm(
        token: String?,
        ar_id: String?,
        ar_cl_opt: String?,
        ar_cl_text: String?,
        ar_cl_att: MultipartBody.Part?,
        ar_status: String?,
    ): Response<GeneralResponse> {
        return apiService.postActivityChecklistForm(token, convertStringToRequestBody(ar_id), convertStringToRequestBody(ar_cl_opt),
            convertStringToRequestBody(ar_cl_text), ar_cl_att, convertStringToRequestBody(ar_status))
    }

    suspend fun postActivityChecklistFormNew(
        token: String?,
        ar_act_id: String?,
        ar_act_cat: String?,
        ar_cl_id: String?,
        ar_cl_opt: String?,
        ar_cl_text: String?,
        ar_cl_att: MultipartBody.Part?,
        ar_cl_status: String?,
    ): Response<GeneralResponse> {
        return apiService.postActivityChecklistFormNew(token, convertStringToRequestBody(ar_act_id), convertStringToRequestBody(ar_act_cat),
            convertStringToRequestBody(ar_cl_id), convertStringToRequestBody(ar_cl_opt), convertStringToRequestBody(ar_cl_text),
            ar_cl_att, convertStringToRequestBody(ar_cl_status))
    }
}