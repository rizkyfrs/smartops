package com.smartfren.smartops.ui.postmortememergency.repository

import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class PMERepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getPostMortemSum(): Response<PMESummaryResponse> {
        return apiService.getPME()
    }

}