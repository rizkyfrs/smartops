package com.smartfren.smartops.ui.login.repository

import com.smartfren.smartops.data.models.LoginRequest
import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.data.models.LoginResponse
import com.smartfren.smartops.data.models.LoginResponseNew
import retrofit2.Response
import javax.inject.Inject

class LoginRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getLogin(loginRequest: LoginRequest, version: String?, fcmToken: String?): Response<LoginResponse> {
        return apiService.userLogin2(loginRequest.email, loginRequest.password, version, fcmToken)
    }

    suspend fun getLoginNew(loginRequest: LoginRequest): Response<LoginResponseNew> {
        return apiService.userLoginNew(loginRequest.email, loginRequest.password)
    }
}