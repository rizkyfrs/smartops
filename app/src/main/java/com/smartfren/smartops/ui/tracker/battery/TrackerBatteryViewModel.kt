package com.smartfren.smartops.ui.tracker.battery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.tracker.repository.TrackerRepository
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
class TrackerBatteryViewModel @Inject constructor(
    application: Application,
    private val repository: TrackerRepository,
) : AndroidViewModel(application) {
    private val _trackerBatteryPDBRegData =
        MutableLiveData<Event<Resource<TrackerBatteryPDBRegionResponse>>>()

    val trackerBatteryPDBRegData: LiveData<Event<Resource<TrackerBatteryPDBRegionResponse>>> =
        _trackerBatteryPDBRegData

    private val _trackerBatteryPDBMWData =
        MutableLiveData<Event<Resource<TrackerBatteryPDBMWResponse>>>()

    val trackerBatteryPDBMWData: LiveData<Event<Resource<TrackerBatteryPDBMWResponse>>> =
        _trackerBatteryPDBMWData

    private val _trackerBatteryPDBRevData =
        MutableLiveData<Event<Resource<TrackerBatteryPDBRevenueResponse>>>()

    val trackerBatteryPDBRevData: LiveData<Event<Resource<TrackerBatteryPDBRevenueResponse>>> =
        _trackerBatteryPDBRevData

    private val _trackerBatteryBBTRegionData =
        MutableLiveData<Event<Resource<TrackerBatteryBBTRegionResponse>>>()

    val trackerBatteryBBTRegionData: LiveData<Event<Resource<TrackerBatteryBBTRegionResponse>>> =
        _trackerBatteryBBTRegionData

    private val _trackerBatteryBBTMWData =
        MutableLiveData<Event<Resource<TrackerBatteryBBTMWResponse>>>()

    val trackerBatteryBBTMWData: LiveData<Event<Resource<TrackerBatteryBBTMWResponse>>> =
        _trackerBatteryBBTMWData

    private val _trackerBatteryBBTRevData =
        MutableLiveData<Event<Resource<TrackerBatteryBBTRevenueResponse>>>()

    val trackerBatteryBBTRevData: LiveData<Event<Resource<TrackerBatteryBBTRevenueResponse>>> =
        _trackerBatteryBBTRevData

    private val _trackerBatteryBATTActionData =
        MutableLiveData<Event<Resource<TrackerBatteryBATTActionResponse>>>()

    val trackerBatteryBATTActionData: LiveData<Event<Resource<TrackerBatteryBATTActionResponse>>> =
        _trackerBatteryBATTActionData

    private val _trackerBatteryBATTStatusData =
        MutableLiveData<Event<Resource<TrackerBatteryBATTStatusResponse>>>()

    val trackerBatteryBATTStatusData: LiveData<Event<Resource<TrackerBatteryBATTStatusResponse>>> =
        _trackerBatteryBATTStatusData

    private val _trackerBatteryBATTSecureData =
        MutableLiveData<Event<Resource<TrackerBatteryBATTSecureResponse>>>()

    val trackerBatteryBATTSecureData: LiveData<Event<Resource<TrackerBatteryBATTSecureResponse>>> =
        _trackerBatteryBATTSecureData

    fun trackerBatteryPDBRegList() = viewModelScope.launch {
        getTrackerBatteryPDBReg()
    }

    fun trackerBatteryPDBMWList(region: String?) = viewModelScope.launch {
        getTrackerBatteryPDBMW(region)
    }

    fun trackerBatteryPDBRevList(region: String?) = viewModelScope.launch {
        getTrackerBatteryPDBRev(region)
    }

    fun trackerBatteryBBTRegList() = viewModelScope.launch {
        getTrackerBatteryBBTReg()
    }

    fun trackerBatteryBBTMWList() = viewModelScope.launch {
        getTrackerBatteryBBTMW()
    }

    fun trackerBatteryBBTRevList() = viewModelScope.launch {
        getTrackerBatteryBBTRev()
    }

    fun trackerBatteryBATTActionList() = viewModelScope.launch {
        getTrackerBatteryBATTAct()
    }

    fun trackerBatteryBATTStatusList() = viewModelScope.launch {
        getTrackerBatteryBATTStatus()
    }

    fun trackerBatteryBATTSecureList() = viewModelScope.launch {
        getTrackerBatteryBATTSec()
    }

    private suspend fun getTrackerBatteryPDBReg() {
        _trackerBatteryPDBRegData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBatteryPDBReg()
                if (response.isSuccessful) {
                    _trackerBatteryPDBRegData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryPDBRegData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryPDBRegData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryPDBRegData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryPDBRegData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerBatteryPDBMW(region: String?) {
        _trackerBatteryPDBMWData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBatteryPDBMW(region)
                if (response.isSuccessful) {
                    _trackerBatteryPDBMWData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryPDBMWData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryPDBMWData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryPDBMWData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryPDBMWData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerBatteryPDBRev(region: String?) {
        _trackerBatteryPDBRevData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBatteryPDBRev(region)
                if (response.isSuccessful) {
                    _trackerBatteryPDBRevData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryPDBRevData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryPDBRevData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryPDBRevData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryPDBRevData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerBatteryBBTReg() {
        _trackerBatteryBBTRegionData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBatteryBBTReg()
                if (response.isSuccessful) {
                    _trackerBatteryBBTRegionData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryBBTRegionData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryBBTRegionData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryBBTRegionData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryBBTRegionData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerBatteryBBTMW() {
        _trackerBatteryBBTMWData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBatteryBBTMW()
                if (response.isSuccessful) {
                    _trackerBatteryBBTMWData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryBBTMWData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryBBTMWData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryBBTMWData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryBBTMWData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerBatteryBBTRev() {
        _trackerBatteryBBTRevData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBatteryBBTRev()
                if (response.isSuccessful) {
                    _trackerBatteryBBTRevData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryBBTRevData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryBBTRevData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryBBTRevData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryBBTRevData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerBatteryBATTAct() {
        _trackerBatteryBATTActionData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBatteryBATTAct()
                if (response.isSuccessful) {
                    _trackerBatteryBATTActionData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryBATTActionData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryBATTActionData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryBATTActionData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryBATTActionData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerBatteryBATTStatus() {
        _trackerBatteryBATTStatusData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBatteryBATTStatus()
                if (response.isSuccessful) {
                    _trackerBatteryBATTStatusData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryBATTStatusData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryBATTStatusData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryBATTStatusData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryBATTStatusData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerBatteryBATTSec() {
        _trackerBatteryBATTSecureData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBatteryBATTSec()
                if (response.isSuccessful) {
                    _trackerBatteryBATTSecureData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryBATTSecureData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryBATTSecureData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryBATTSecureData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryBATTSecureData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}