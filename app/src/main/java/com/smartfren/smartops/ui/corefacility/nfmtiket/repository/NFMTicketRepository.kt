package com.smartfren.smartops.ui.corefacility.nfmtiket.repository

import com.smartfren.smartops.data.models.NFMTicketDetailResponse
import com.smartfren.smartops.data.models.NFMTiketResponse
import com.smartfren.smartops.data.network.ApiService
import com.smartfren.smartops.data.models.GeneralResponse
import retrofit2.Response
import javax.inject.Inject

class NFMTicketRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListNFMTicket(token: String?, page: String?): Response<NFMTiketResponse> {
        return apiService.getNFMTicket(token, page)
    }

    suspend fun getDetailNFMTicket(token: String?, ticket_id: String?): Response<NFMTicketDetailResponse> {
        return apiService.getNFMTicketDetail(token, ticket_id)
    }

    suspend fun postValidate(token: String?, ticket_id: String?): Response<GeneralResponse> {
        return apiService.postValidateNFMTicket(token, ticket_id)
    }
}