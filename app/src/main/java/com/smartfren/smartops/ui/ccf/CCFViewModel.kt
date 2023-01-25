package com.smartfren.smartops.ui.ccf

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.ccf.repository.CCFRepository
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
class CCFViewModel @Inject constructor(
    application: Application,
    private val repository: CCFRepository,
) : AndroidViewModel(application) {
    private val _ccfdData = MutableLiveData<Event<Resource<CCFDailyResponse>>>()
    private val _ccfwData = MutableLiveData<Event<Resource<CCFWeeklyResponse>>>()
    private val _ccfmData = MutableLiveData<Event<Resource<CCFMonthlyResponse>>>()
    private val _ccfnmData = MutableLiveData<Event<Resource<CCFNMonthlyResponse>>>()
    private val _ccfyData = MutableLiveData<Event<Resource<CCFYearlyResponse>>>()

    val ccfdData: LiveData<Event<Resource<CCFDailyResponse>>> = _ccfdData
    val ccfwData: LiveData<Event<Resource<CCFWeeklyResponse>>> = _ccfwData
    val ccfmData: LiveData<Event<Resource<CCFMonthlyResponse>>> = _ccfmData
    val ccfnmData: LiveData<Event<Resource<CCFNMonthlyResponse>>> = _ccfnmData
    val ccfyData: LiveData<Event<Resource<CCFYearlyResponse>>> = _ccfyData

    fun setCCFD() = viewModelScope.launch {
        getCCFD()
    }

    fun setCCFW() = viewModelScope.launch {
        getCCFW()
    }

    fun setCCFM() = viewModelScope.launch {
        getCCFM()
    }

    fun setCCFNM() = viewModelScope.launch {
        getCCFNM()
    }

    fun setCCFY() = viewModelScope.launch {
        getCCFY()
    }

    private suspend fun getCCFD() {
        _ccfdData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getCCFDaily()
                if (response.isSuccessful) {
                    _ccfdData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _ccfdData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _ccfdData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _ccfdData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _ccfdData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getCCFW() {
        _ccfwData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getCCFWeekly()
                if (response.isSuccessful) {
                    _ccfwData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _ccfwData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _ccfwData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _ccfwData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _ccfwData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getCCFM() {
        _ccfmData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getCCFMonthly()
                if (response.isSuccessful) {
                    _ccfmData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _ccfmData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _ccfmData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _ccfmData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _ccfmData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getCCFNM() {
        _ccfnmData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getCCFNMonthly()
                if (response.isSuccessful) {
                    _ccfnmData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _ccfnmData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _ccfnmData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _ccfnmData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _ccfnmData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getCCFY() {
        _ccfyData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getCCFYearly()
                if (response.isSuccessful) {
                    _ccfyData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _ccfyData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _ccfyData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _ccfyData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _ccfyData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}