package com.smartfren.smartops.ui.workordermanagement

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.IncidentWODetailResponse
import com.smartfren.smartops.data.models.IncidentWOListResponse
import com.smartfren.smartops.ui.workordermanagement.repository.WorkorderRepository
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
class WorkorderViewModel @Inject constructor(
    application: Application,
    private val repository: WorkorderRepository,
) : AndroidViewModel(application) {
    private val _listWO = MutableLiveData<Event<Resource<IncidentWOListResponse>>>()
    val listWO: LiveData<Event<Resource<IncidentWOListResponse>>> = _listWO

    private val _viewWO = MutableLiveData<Event<Resource<IncidentWODetailResponse>>>()
    val viewWO: LiveData<Event<Resource<IncidentWODetailResponse>>> = _viewWO

    private val _editWO = MutableLiveData<Event<Resource<IncidentWODetailResponse>>>()
    val editWO: LiveData<Event<Resource<IncidentWODetailResponse>>> = _editWO

    fun setListWO(status: String?, start: String?, recperpage: String?) = viewModelScope.launch {
        getListWO(status, start, recperpage)
    }

    fun setViewWO(key: String?) = viewModelScope.launch {
        getViewWO(key)
    }

    fun setEditWO(
        key: String?, incwo_status: String?, incwo_departure_time: String?, incwo_departure_by: String?, incwo_arrive_time: String?,
        incwo_arrive_by: String?, incwo_onsite_coordinate: String?, incwo_troubleshoot_time: String?, incwo_troubleshoot_by: String?,
        incwo_troubleshoot_action: String?, incwo_rca1: String?, incwo_rca2: String?, incwo_rca3: String?, incwo_action: String?, incwo_pic_team: String?
    ) = viewModelScope.launch {
        postEditWO(
            key, incwo_status, incwo_departure_time, incwo_departure_by, incwo_arrive_time, incwo_arrive_by, incwo_onsite_coordinate, incwo_troubleshoot_time,
            incwo_troubleshoot_by, incwo_troubleshoot_action, incwo_rca1, incwo_rca2, incwo_rca3, incwo_action, incwo_pic_team
        )
    }

    private suspend fun getListWO(status: String?, start: String?, recperpage: String?) {
        _listWO.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListWorkOrder(status, start, recperpage)
                if (response.isSuccessful) {
                    if (response.body()?.tbIncidentWorkorder.isNullOrEmpty()) {
                        toast(getApplication(), "List is empty.")
                    }
                    _listWO.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    if (response.code() == 405) {
                        _listWO.postValue(Event(Resource.Error("Token invalid, please re-login.")))
                        MainApp.instance.onSessionExpired(getApplication())
                    } else {
                        _listWO.postValue(Event(Resource.Error(response.message())))
                    }
                }
            } else {
                _listWO.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listWO.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listWO.postValue(Event(Resource.Error(t.message!!)))
                    if (_listWO.value?.getContentIfNotHandled()?.data == null) {
                        toast(getApplication(), "List empty.")
                    } else {
                        toast(getApplication(), t.message!!)
                    }
                }
            }
        }
    }

    private suspend fun getViewWO(key: String?) {
        _viewWO.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getViewWorkOrder(key)
                if (response.isSuccessful) {
                    _viewWO.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _viewWO.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _viewWO.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _viewWO.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _viewWO.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postEditWO(
        key: String?, incwo_status: String?, incwo_departure_time: String?, incwo_departure_by: String?, incwo_arrive_time: String?,
        incwo_arrive_by: String?, incwo_onsite_coordinate: String?, incwo_troubleshoot_time: String?, incwo_troubleshoot_by: String?,
        incwo_troubleshoot_action: String?, incwo_rca1: String?, incwo_rca2: String?, incwo_rca3: String?, incwo_action: String?, incwo_pic_team: String?
    ) {
        _editWO.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postEditWorkOrder(
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
                    incwo_action,
                    incwo_pic_team
                )
                if (response.isSuccessful) {
                    _editWO.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _editWO.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _editWO.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _editWO.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _editWO.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}