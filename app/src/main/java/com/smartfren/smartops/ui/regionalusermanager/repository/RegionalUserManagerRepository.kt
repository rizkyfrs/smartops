package com.smartfren.smartops.ui.regionalusermanager.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Path
import javax.inject.Inject

class RegionalUserManagerRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListRegionalUserManager(
        start: String?,
        recperpage: String?,
        u_level: String?,
        id_parrent: String?
    ): Response<RegionalUserManagerListResponse> {
        return apiService.getListRegionalUserManager(start, recperpage, u_level, id_parrent)
    }

    suspend fun getViewRegionalUserManager(key: String?, ): Response<RegionalUserManagerViewResponse> {
        return apiService.getViewRegionalUserManager(key)
    }

    suspend fun getListRegionalUserLevel(): Response<RegionalUserLevelResponse> {
        return apiService.getListRegionalUserLevel()
    }

    suspend fun postAddRegionalUserManager(
        id_user: String?,
        u_region: String?,
        id_parrent: String?,
        u_nik: String?,
        username: String?,
        password: String?,
        u_name: String?,
        u_phone: String?,
        u_level: String?,
        u_mail: String?,
        u_activation: String?,
        u_registrars: String?,
        u_registrationdate: String?
    ): Response<RegionalUserManagerAddResponse> {
        return apiService.postAddRegionalUserManager(
            u_region, id_parrent, u_nik, username, password, u_name, u_phone, u_level, u_mail, u_activation, u_registrars, u_registrationdate
        )
    }


    suspend fun postEditRegionalUserManager(
        key: String?,
        id_user: String?,
        u_company: String?,
        u_region: String?,
        id_parrent: String?,
        u_nik: String?,
        username: String?,
        password: String?,
        u_name: String?,
        u_phone: String?,
        u_level: String?,
        u_mail: String?,
        u_activation: String?
    ): Response<RegionalUserManagerAddResponse> {
        return apiService.postEditRegionalUserManager(
            key, id_user, u_company, u_region, id_parrent, u_nik, username, password, u_name, u_phone, u_level, u_mail, u_activation)
    }
}