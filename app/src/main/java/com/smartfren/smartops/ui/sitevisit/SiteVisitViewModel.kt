package com.smartfren.smartops.ui.sitevisit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.SiteVisitLogDetailResponse
import com.smartfren.smartops.data.models.SiteVisitLogRepose
import com.smartfren.smartops.ui.sitevisit.repository.SiteVisitRepository
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
class SiteVisitViewModel @Inject constructor(
    application: Application,
    private val repository: SiteVisitRepository,
) : AndroidViewModel(application) {
    private val _siteVisitLogData = MutableLiveData<Event<Resource<SiteVisitLogRepose>>>()
    val siteVisitLogData: LiveData<Event<Resource<SiteVisitLogRepose>>> = _siteVisitLogData

    private val _siteVisitLogSearchData = MutableLiveData<Event<Resource<SiteVisitLogDetailResponse>>>()
    val siteVisitLogSearchData: LiveData<Event<Resource<SiteVisitLogDetailResponse>>> = _siteVisitLogSearchData

    fun siteVisitLog(start: String?, recperpage: String?) = viewModelScope.launch {
        getSiteVisitLogs(start, recperpage)
    }

    fun siteVisitLogSearch(key: String?) = viewModelScope.launch {
        getSiteVisitLogsSearch(key)
    }

    private suspend fun getSiteVisitLogs(start: String?, recperpage: String?) {
        _siteVisitLogData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteVisitLog(start, recperpage)
                if (response.isSuccessful) {
                    _siteVisitLogData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteVisitLogData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteVisitLogData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteVisitLogData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteVisitLogData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSiteVisitLogsSearch(key: String?) {
        _siteVisitLogSearchData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteVisitLogSearch(key)
                if (response.isSuccessful) {
                    _siteVisitLogSearchData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteVisitLogSearchData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteVisitLogSearchData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteVisitLogSearchData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteVisitLogSearchData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}