package com.smartfren.smartops.ui.corefacility.nfmtiket

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.NFMTicketDetailResponse
import com.smartfren.smartops.data.models.NFMTiketResponse
import com.smartfren.smartops.data.models.GeneralResponse
import com.smartfren.smartops.ui.corefacility.nfmtiket.repository.NFMTicketRepository
import com.smartfren.smartops.util.Event
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hasInternetConnection
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class NFMTicketViewModel @Inject constructor(
    application: Application,
    private val repository: NFMTicketRepository,
) : AndroidViewModel(application) {
    private val _listNFMTicket = MutableLiveData<Event<Resource<NFMTiketResponse>>>()
    private val _detailNFMTicket = MutableLiveData<Event<Resource<NFMTicketDetailResponse>>>()
    private val _validate = MutableLiveData<Event<Resource<GeneralResponse>>>()

    val nfmTicketData: LiveData<Event<Resource<NFMTiketResponse>>> = _listNFMTicket
    val nfmTicketDetailData: LiveData<Event<Resource<NFMTicketDetailResponse>>> = _detailNFMTicket
    val validateData: LiveData<Event<Resource<GeneralResponse>>> = _validate

    fun listNFMTicket(token: String?, page: String?) = viewModelScope.launch {
        getListNFMTicket(token, page)
    }

    fun detailNFMTicket(token: String?, ticket_id: String?) = viewModelScope.launch {
        getDetailCCFReport(token, ticket_id)
    }

    fun validate(token: String?, report_id: String?) = viewModelScope.launch {
        postValidate(token, report_id)
    }

    private suspend fun getListNFMTicket(token: String?, page: String?) {
        _listNFMTicket.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListNFMTicket(token, page)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listNFMTicket.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listNFMTicket.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listNFMTicket.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listNFMTicket.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listNFMTicket.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getDetailCCFReport(token: String?, ticket_id: String?) {
        _detailNFMTicket.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getDetailNFMTicket(token, ticket_id)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _detailNFMTicket.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _detailNFMTicket.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _detailNFMTicket.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _detailNFMTicket.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _detailNFMTicket.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postValidate(token: String?, ticket_id: String?) {
        _validate.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postValidate(token, ticket_id)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _validate.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _validate.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _validate.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _validate.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _validate.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}