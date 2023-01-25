package com.smartfren.smartops.ui.stolen.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.utils.convertStringToRequestBody
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class StolenRepository @Inject constructor(private val apiService: ApiService) {
    suspend fun getListStolenReport(start: String?, recperpage: String?): Response<StolenListReportResponse> {
        return apiService.getListStolenReport(start, recperpage)
    }

    suspend fun getViewStolenReport(key: String?): Response<StolenViewReportResponse> {
        return apiService.getViewStolenReport(key)
    }

    suspend fun getStolenReportChart(token: String?): Response<StolenReportResponse> {
        return apiService.getStolenReport(token)
    }

    suspend fun getStolenReportCurrentMonth(token: String?): Response<StolenReportCurrentMonthResponse> {
        return apiService.getStolenReportCurrentMonth(token)
    }

    suspend fun getStolenReportKRI(token: String?): Response<SiteStolenKRIResponse> {
        return apiService.getSiteStolenKRI(token)
    }

    suspend fun postAddStolen(
        sr_region: String?, sr_site_id: String?, sr_date_of_loss: String?, sr_existing_security: String?, sr_stolen_material: String?,
        sr_photo: String?, sr_alarm: String?, sr_kronologi: String?, sr_doc_kronologi: String?, sr_lkp: String?, sr_doc_lkp: String?,
        sr_recovery: String?, sr_investigation: String?, sr_doc_investigation: String?, sr_registrars: String?, sr_registerdate: String?
    ): Response<StolenViewReportResponse> {
        return apiService.postAddStolen(
            sr_region,
            sr_site_id,
            sr_date_of_loss,
            sr_existing_security,
            sr_stolen_material,
            sr_photo,
            sr_alarm,
            sr_kronologi,
            sr_doc_kronologi,
            sr_lkp,
            sr_doc_lkp,
            sr_recovery,
            sr_investigation,
            sr_doc_investigation,
            sr_registrars,
            sr_registerdate
        )
    }

    suspend fun postUploadStolen(
        token: String?,
        table_name: String?,
        table_key: String?,
        table_key_id: String?,
        table_column: String?,
        file_dir: String?,
        file_load: MultipartBody.Part?
    ): Response<StolenViewReportResponse> {
        return apiService.postUploadFileStolen(
            token, convertStringToRequestBody(table_name), convertStringToRequestBody(table_key),
            convertStringToRequestBody(table_key_id), convertStringToRequestBody(table_column), convertStringToRequestBody(file_dir),
            file_load
        )
    }
}