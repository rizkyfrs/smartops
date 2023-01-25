package com.smartfren.smartops.ui.nfm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.IncidentWOListResponse
import com.smartfren.smartops.data.models.NFMAlarmResponse
import com.smartfren.smartops.data.models.NFMIoTResponse
import com.smartfren.smartops.data.models.NFMSNMPResponse
import com.smartfren.smartops.ui.nfm.repository.NFMRepository
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
class NFMViewModel @Inject constructor(
    application: Application,
    private val repository: NFMRepository,
) : AndroidViewModel(application) {
    private val _listNFM = MutableLiveData<Event<Resource<NFMIoTResponse>>>()
    val listNFM: LiveData<Event<Resource<NFMIoTResponse>>> = _listNFM

    private val _detailNFM = MutableLiveData<Event<Resource<NFMSNMPResponse>>>()
    val detailNFM: LiveData<Event<Resource<NFMSNMPResponse>>> = _detailNFM

    private val _alarmNFM = MutableLiveData<Event<Resource<NFMAlarmResponse>>>()
    val alarmNFM: LiveData<Event<Resource<NFMAlarmResponse>>> = _alarmNFM

    fun setListNFM(iot_id: String?) = viewModelScope.launch {
        getListNFM(iot_id)
    }

    fun setDetailNFM(iot_id: String?, ne_id: String?) = viewModelScope.launch {
        getDetailNFM(iot_id, ne_id)
    }

    fun setAlarmNFM() = viewModelScope.launch {
        getAlarmNFM()
    }

    private suspend fun getListNFM(iot_id: String?) {
        _listNFM.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListNFM(iot_id)
                if (response.isSuccessful) {
                    if (response.body()?.ne.isNullOrEmpty()) {
                        toast(getApplication(), "List is empty.")
                    }
                    _listNFM.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listNFM.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listNFM.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listNFM.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listNFM.postValue(Event(Resource.Error(t.message!!)))
                    if (_listNFM.value?.getContentIfNotHandled()?.data == null) {
                        toast(getApplication(), "List empty.")
                    } else {
                        toast(getApplication(), t.message!!)
                    }
                }
            }
        }
    }

    private suspend fun getDetailNFM(iot_id: String?, ne_id: String?) {
        _detailNFM.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getDetailNFM(iot_id, ne_id)
                if (response.isSuccessful) {
                    if (response.body()?.snmpVal.isNullOrEmpty()) {
                        toast(getApplication(), "List is empty.")
                    }
                    _detailNFM.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _detailNFM.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _detailNFM.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _detailNFM.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _detailNFM.postValue(Event(Resource.Error(t.message!!)))
                    if (_detailNFM.value?.getContentIfNotHandled()?.data == null) {
                        toast(getApplication(), "List empty.")
                    } else {
                        toast(getApplication(), t.message!!)
                    }
                }
            }
        }
    }

    private suspend fun getAlarmNFM() {
        _alarmNFM.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getAlarmNFM()
                if (response.isSuccessful) {
                    if (response.body()?.getAlarm().isNullOrEmpty()) {
                        toast(getApplication(), "List is empty.")
                    }
                    _alarmNFM.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _alarmNFM.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _alarmNFM.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _alarmNFM.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _alarmNFM.postValue(Event(Resource.Error(t.message!!)))
                    if (_alarmNFM.value?.getContentIfNotHandled()?.data == null) {
                        toast(getApplication(), "List empty.")
                    } else {
                        toast(getApplication(), t.message!!)
                    }
                }
            }
        }
    }
}