package com.smartfren.smartops.ui.sitemanagementinfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.SiteManagementInfoDetailResponse
import com.smartfren.smartops.data.models.SiteManagementInfoResponse
import com.smartfren.smartops.ui.sitemanagementinfo.repository.SiteManagementInfoRepository
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
class SiteManagementInfoViewModel @Inject constructor(
    application: Application,
    private val repository: SiteManagementInfoRepository,
) : AndroidViewModel(application) {
    private val _siteManagementInfoData = MutableLiveData<Event<Resource<SiteManagementInfoResponse>>>()
    val siteManagementInfoData: LiveData<Event<Resource<SiteManagementInfoResponse>>> = _siteManagementInfoData

    private val _siteManagementInfoDetailData = MutableLiveData<Event<Resource<SiteManagementInfoDetailResponse>>>()
    val siteManagementInfoDetailData: LiveData<Event<Resource<SiteManagementInfoDetailResponse>>> = _siteManagementInfoDetailData

    fun siteManagementInfo(start: String?, recperpage: String?) = viewModelScope.launch {
        getSiteManagementInfo(start, recperpage)
    }

    fun siteManagementInfoDetail(key: String?) = viewModelScope.launch {
        getSiteManagementInfoDetail(key)
    }

    private suspend fun getSiteManagementInfo(start: String?, recperpage: String?) {
        _siteManagementInfoData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteManagementInfo(start, recperpage)
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _siteManagementInfoData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteManagementInfoData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteManagementInfoData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteManagementInfoData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteManagementInfoData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSiteManagementInfoDetail(key: String?) {
        _siteManagementInfoDetailData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteManagementInfoDetail(key)
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _siteManagementInfoDetailData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteManagementInfoDetailData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteManagementInfoDetailData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteManagementInfoDetailData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteManagementInfoDetailData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}