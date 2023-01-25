package com.smartfren.smartops.ui.home.repository

import com.smartfren.smartops.data.models.CheckInResponse
import com.smartfren.smartops.data.models.FLMStatisticRepsonse
import com.smartfren.smartops.data.models.GeneralResponse
import com.smartfren.smartops.data.models.PersonalInfoResponse
import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.utils.convertStringToRequestBody
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class HomepageRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getPersonalData(token: String?): Response<PersonalInfoResponse> {
        return apiService.personalInfo(token)
    }

    suspend fun getFLMStatisticData(token: String?, type: String?): Response<FLMStatisticRepsonse> {
        return apiService.getFLMStatistic(token, type)
    }

    suspend fun postPersonalInfo(
        token: String?,
        nik: String?,
        name: String?,
        email: String?,
        number: String?,
        pp: MultipartBody.Part?
    ): Response<GeneralResponse> {
        return apiService.postUpdatePersonalInfo(
            token,
            convertStringToRequestBody(nik),
            convertStringToRequestBody(name),
            convertStringToRequestBody(email),
            convertStringToRequestBody(number),
            pp
        )
    }

    suspend fun postChangePassword(
        token: String?,
        new_password: String?
    ): Response<GeneralResponse> {
        return apiService.postChangePassword(token, new_password)
    }

    suspend fun postLogout(token: String?): Response<GeneralResponse> {
        return apiService.logout(token)
    }

    suspend fun postUpdateTagging(token: String?, new_tagging: String?): Response<GeneralResponse> {
        return apiService.postUpdateTagging(token, new_tagging)
    }

    suspend fun postCheckIn(
        date: String?,
        reg: String?,
        site: String?,
        device_imei: String?,
        device_custodian: String?,
        lat: String?,
        long: String?
    ): Response<CheckInResponse> {
        return apiService.postCheckInPerson(date, reg, site, device_imei, device_custodian, long, lat)
    }

    suspend fun postCheckOut(
        checkinid: String?,
        lat: String?,
        long: String?
    ): Response<CheckInResponse> {
        return apiService.postCheckOutPerson(checkinid, long, lat)
    }
}