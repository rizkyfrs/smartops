package com.smartfren.smartops.ui.stom

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.stom.repository.STOMRepository
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
class STOMViewModel @Inject constructor(
    application: Application,
    private val repository: STOMRepository,
) : AndroidViewModel(application) {
    private val _stomLeaseAllTPData = MutableLiveData<Event<Resource<STOMLeaseAllTPResponse>>>()
    val stomLeaseAllTPData: LiveData<Event<Resource<STOMLeaseAllTPResponse>>> = _stomLeaseAllTPData

    private val _stomLeaseAllDLData = MutableLiveData<Event<Resource<STOMLeaseAllDLResponse>>>()
    val stomLeaseAllDLData: LiveData<Event<Resource<STOMLeaseAllDLResponse>>> = _stomLeaseAllDLData

    private val _stomLeaseAllIBData = MutableLiveData<Event<Resource<STOMLeaseAllIBResponse>>>()
    val stomLeaseAllIBData: LiveData<Event<Resource<STOMLeaseAllIBResponse>>> = _stomLeaseAllIBData

    private val _stomLeaseAllOtherData = MutableLiveData<Event<Resource<STOMLeaseAllOtherResponse>>>()
    val stomLeaseAllOtherData: LiveData<Event<Resource<STOMLeaseAllOtherResponse>>> = _stomLeaseAllOtherData

    private val _stomMilestoneTPData = MutableLiveData<Event<Resource<STOMMilestoneTPResponse>>>()
    val stomMilestoneTPData: LiveData<Event<Resource<STOMMilestoneTPResponse>>> = _stomMilestoneTPData

    private val _stomMilestoneDLData = MutableLiveData<Event<Resource<STOMMilestoneDLResponse>>>()
    val stomMilestoneDLData: LiveData<Event<Resource<STOMMilestoneDLResponse>>> = _stomMilestoneDLData

    private val _stomMilestoneIBData = MutableLiveData<Event<Resource<STOMMilestoneIBResponse>>>()
    val stomMilestoneIBData: LiveData<Event<Resource<STOMMilestoneIBResponse>>> = _stomMilestoneIBData

    fun stomLeaseAllTPList() = viewModelScope.launch {
        getLeaseAllTP()
    }

    fun stomLeaseAllDLList() = viewModelScope.launch {
        getLeaseAllDL()
    }

    fun stomLeaseAllIBList() = viewModelScope.launch {
        getLeaseAllIB()
    }

    fun stomLeaseAllOtherList() = viewModelScope.launch {
        getLeaseAllOther()
    }

    fun stomMilestoneTPList() = viewModelScope.launch {
        getMilestoneTP()
    }

    fun stomMilestoneDLList() = viewModelScope.launch {
        getMilestoneDL()
    }

    fun stomMilestoneIBList() = viewModelScope.launch {
        getMilestoneIB()
    }

    private suspend fun getLeaseAllTP() {
        _stomLeaseAllTPData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSTOMLeaseAllTP()
                if (response.isSuccessful) {
                    _stomLeaseAllTPData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stomLeaseAllTPData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stomLeaseAllTPData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stomLeaseAllTPData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stomLeaseAllTPData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getLeaseAllDL() {
        _stomLeaseAllDLData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSTOMLeaseDL()
                if (response.isSuccessful) {
                    _stomLeaseAllDLData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stomLeaseAllDLData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stomLeaseAllDLData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stomLeaseAllDLData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stomLeaseAllDLData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getLeaseAllIB() {
        _stomLeaseAllIBData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSTOMLeaseIB()
                if (response.isSuccessful) {
                    _stomLeaseAllIBData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stomLeaseAllIBData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stomLeaseAllIBData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stomLeaseAllIBData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stomLeaseAllIBData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getLeaseAllOther() {
        _stomLeaseAllOtherData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSTOMLeaseOther()
                if (response.isSuccessful) {
                    _stomLeaseAllOtherData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stomLeaseAllOtherData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stomLeaseAllOtherData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stomLeaseAllOtherData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stomLeaseAllOtherData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getMilestoneTP() {
        _stomMilestoneTPData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSTOMMilestoneTP()
                if (response.isSuccessful) {
                    _stomMilestoneTPData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stomMilestoneTPData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stomMilestoneTPData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stomMilestoneTPData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stomMilestoneTPData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getMilestoneDL() {
        _stomMilestoneDLData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSTOMMilestoneDL()
                if (response.isSuccessful) {
                    _stomMilestoneDLData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stomMilestoneDLData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stomMilestoneDLData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stomMilestoneDLData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stomMilestoneDLData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getMilestoneIB() {
        _stomMilestoneIBData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSTOMMilestoneIB()
                if (response.isSuccessful) {
                    _stomMilestoneIBData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stomMilestoneIBData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stomMilestoneIBData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stomMilestoneIBData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stomMilestoneIBData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}