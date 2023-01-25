package com.smartfren.smartops.ui.reportpm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.ReportPMResponse
import com.smartfren.smartops.data.models.SiteInfoResponse
import com.smartfren.smartops.ui.reportpm.repository.ReportPMRepository
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
class ReportPMViewModel @Inject constructor(
    application: Application,
    private val repository: ReportPMRepository,
) : AndroidViewModel(application) {
    private val _getReportSite = MutableLiveData<Event<Resource<SiteInfoResponse>>>()
    private val _getReportPM = MutableLiveData<Event<Resource<ReportPMResponse>>>()

    val getReportSiteData: LiveData<Event<Resource<SiteInfoResponse>>> = _getReportSite
    val getReportPMData: LiveData<Event<Resource<ReportPMResponse>>> = _getReportPM

    fun getReportSiteTask(token: String?, site: String?) = viewModelScope.launch {
        getReportSite(token, site)
    }

    fun getReportPMTask(token: String?, site: String?, date: String?) = viewModelScope.launch {
        getReportPM(token, site, date)
    }

    private suspend fun getReportSite(token: String?, site: String?) {
        _getReportSite.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getReportSite(token, site)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _getReportSite.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _getReportSite.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _getReportSite.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _getReportSite.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _getReportSite.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }



    private suspend fun getReportPM(token: String?, site: String?, date: String?) {
        _getReportPM.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getReportPM(token, site, date)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _getReportPM.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _getReportPM.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _getReportPM.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _getReportPM.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _getReportPM.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}