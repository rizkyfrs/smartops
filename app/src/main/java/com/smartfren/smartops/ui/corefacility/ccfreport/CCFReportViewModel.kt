package com.smartfren.smartops.ui.corefacility.ccfreport

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.CCFReportResponse
import com.smartfren.smartops.data.models.CFFReportDetailResponse
import com.smartfren.smartops.data.models.GeneralResponse
import com.smartfren.smartops.ui.corefacility.ccfreport.repository.CCFReportRepository
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
class CCFReportViewModel @Inject constructor(
    application: Application,
    private val repository: CCFReportRepository,
) : AndroidViewModel(application) {
    private val _listCCFReport = MutableLiveData<Event<Resource<CCFReportResponse>>>()
    private val _detailCCFReport = MutableLiveData<Event<Resource<CFFReportDetailResponse>>>()
    private val _validate = MutableLiveData<Event<Resource<GeneralResponse>>>()

    val ccfReportData: LiveData<Event<Resource<CCFReportResponse>>> = _listCCFReport
    val ccfReportDetailData: LiveData<Event<Resource<CFFReportDetailResponse>>> = _detailCCFReport
    val validateData: LiveData<Event<Resource<GeneralResponse>>> = _validate

    fun listCCFReport(token: String?, page: String?) = viewModelScope.launch {
        getListCCFReport(token, page)
    }

    fun detailCCFReport(token: String?, report_id: String?) = viewModelScope.launch {
        getDetailCCFReport(token, report_id)
    }


    fun validate(token: String?, report_id: String?) = viewModelScope.launch {
        postValidate(token, report_id)
    }

    private suspend fun getListCCFReport(token: String?, page: String?) {
        _listCCFReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListCCFReport(token, page)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listCCFReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listCCFReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listCCFReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listCCFReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }

            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listCCFReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }

            }

        }
    }

    private suspend fun getDetailCCFReport(token: String?, report_id: String?) {
        _detailCCFReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getDetailCCFReport(token, report_id)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _detailCCFReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _detailCCFReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _detailCCFReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _detailCCFReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }

            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _detailCCFReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }

            }

        }
    }

    private suspend fun postValidate(token: String?, report_id: String?) {
        _validate.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postValidate(token, report_id)
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