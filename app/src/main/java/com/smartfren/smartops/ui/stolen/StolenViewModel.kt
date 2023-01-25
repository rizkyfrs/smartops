package com.smartfren.smartops.ui.stolen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.stolen.repository.StolenRepository
import com.smartfren.smartops.util.Event
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hasInternetConnection
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class StolenViewModel @Inject constructor(
    application: Application,
    private val repository: StolenRepository,
) : AndroidViewModel(application) {
    private val _listStolenReport = MutableLiveData<Event<Resource<StolenListReportResponse>>>()
    val listStolenReport: LiveData<Event<Resource<StolenListReportResponse>>> = _listStolenReport

    private val _viewStolenReport = MutableLiveData<Event<Resource<StolenViewReportResponse>>>()
    val viewStolenReport: LiveData<Event<Resource<StolenViewReportResponse>>> = _viewStolenReport

    private val _postStolenReport = MutableLiveData<Event<Resource<StolenViewReportResponse>>>()
    val postStolenReport: LiveData<Event<Resource<StolenViewReportResponse>>> = _postStolenReport

    private val _postUploadFileStolen = MutableLiveData<Event<Resource<StolenViewReportResponse>>>()
    val postUploadFileStolen: LiveData<Event<Resource<StolenViewReportResponse>>> = _postUploadFileStolen

    private val _stolenReport = MutableLiveData<Event<Resource<StolenReportResponse>>>()
    val stolenReport: LiveData<Event<Resource<StolenReportResponse>>> = _stolenReport

    private val _stolenReportCurrentMonth = MutableLiveData<Event<Resource<StolenReportCurrentMonthResponse>>>()
    val stolenReportCurrentMonth: LiveData<Event<Resource<StolenReportCurrentMonthResponse>>> = _stolenReportCurrentMonth

    private val _stolenKRIReport = MutableLiveData<Event<Resource<SiteStolenKRIResponse>>>()
    val stolenKRIReport: LiveData<Event<Resource<SiteStolenKRIResponse>>> = _stolenKRIReport

    private val _region = MutableLiveData<String?>()
    val region: LiveData<String?> = _region

    fun setRegionSelected(regionSelection: String?) {
        _region.value = regionSelection
    }

    fun setRegion(): String {
        val regions = region.value
        return regions.toString()
    }

    fun setRegionCode(): String {
        var regionCodes = ""
        when (region.value) {
            "JB" -> {
                regionCodes = "1"
            }
            "NS" -> {
                regionCodes = "2"
            }
            "SS" -> {
                regionCodes = "3"
            }
            "WJ" -> {
                regionCodes = "4"
            }
            "CJ" -> {
                regionCodes = "5"
            }
            "EJ" -> {
                regionCodes = "6"
            }
            "SK" -> {
                regionCodes = "7"
            }
        }
        return regionCodes
    }

    fun setRegionName(): String {
        var regionNames = ""
        when (region.value) {
            "1" -> {
                regionNames = "JB"
            }
            "2" -> {
                regionNames = "NS"
            }
            "3" -> {
                regionNames = "SS"
            }
            "4" -> {
                regionNames = "WJ"
            }
            "5" -> {
                regionNames = "CJ"
            }
            "6" -> {
                regionNames = "EJ"
            }
            "7" -> {
                regionNames = "SK"
            }
        }
        return regionNames
    }

    fun setStolenReport(token: String?) = viewModelScope.launch {
        getStolenReport(token)
    }

    fun setStolenReportCurrentMonth(token: String?) = viewModelScope.launch {
        getStolenReportCurrentMonth(token)
    }

    fun setStolenKRIReport(token: String?) = viewModelScope.launch {
        getStolenKRIReport(token)
    }

    fun setListStolenReport(start: String?, recperpage: String?) = viewModelScope.launch {
        getListStolenReport(start, recperpage)
    }

    fun setViewStolenReport(key: String?) = viewModelScope.launch {
        getViewStolenReport(key)
    }

    fun setPostStolenReport(
        sr_region: String?, sr_site_id: String?, sr_date_of_loss: String?, sr_existing_security: String?, sr_stolen_material: String?,
        sr_photo: String?, sr_alarm: String?, sr_kronologi: String?, sr_doc_kronologi: String?, sr_lkp: String?, sr_doc_lkp: String?,
        sr_recovery: String?, sr_investigation: String?,sr_doc_investigation: String?, sr_registrars: String?, sr_registerdate: String?
    ) = viewModelScope.launch {
        getPostStolenReport(
            sr_region,
            sr_site_id,
            sr_date_of_loss,
            sr_existing_security,
            sr_stolen_material,
            sr_photo,
            sr_alarm,
            sr_kronologi,
            sr_doc_kronologi,
            sr_lkp,
            sr_doc_lkp,
            sr_recovery,
            sr_investigation,
            sr_doc_investigation,
            sr_registrars,
            sr_registerdate
        )
    }

    fun setPostUploadFileStolen(
        token: String?,
        table_name: String?,
        table_key: String?,
        table_key_id: String?,
        table_column: String?,
        file_dir: String?,
        file_load: MultipartBody.Part?
    ) = viewModelScope.launch {
        getPostUploadFileStolen(
            token, table_name, table_key, table_key_id, table_column, file_dir, file_load
        )
    }

    private suspend fun getListStolenReport(start: String?, recperpage: String?) {
        _listStolenReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListStolenReport(start, recperpage)
                if (response.isSuccessful) {
                    if (response.body()?.cvStolenReport == null || response.body()?.cvStolenReport!!.isEmpty()) {
                        toast(getApplication(), "List is empty.")
                    }
                    _listStolenReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listStolenReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listStolenReport.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listStolenReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listStolenReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getViewStolenReport(key: String?) {
        _viewStolenReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getViewStolenReport(key)
                if (response.isSuccessful) {
                    _viewStolenReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _viewStolenReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _viewStolenReport.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _viewStolenReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _viewStolenReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getPostStolenReport(
        sr_region: String?, sr_site_id: String?, sr_date_of_loss: String?, sr_existing_security: String?, sr_stolen_material: String?,
        sr_photo: String?, sr_alarm: String?, sr_kronologi: String?, sr_doc_kronologi: String?, sr_lkp: String?, sr_doc_lkp: String?,
        sr_recovery: String?, sr_investigation: String?, sr_doc_investigation: String?, sr_registrars: String?, sr_registerdate: String?
    ) {
        _postStolenReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postAddStolen(
                    sr_region,
                    sr_site_id,
                    sr_date_of_loss,
                    sr_existing_security,
                    sr_stolen_material,
                    sr_photo,
                    sr_alarm,
                    sr_kronologi,
                    sr_doc_kronologi,
                    sr_lkp,
                    sr_doc_lkp,
                    sr_recovery,
                    sr_investigation,
                    sr_doc_investigation,
                    sr_registrars,
                    sr_registerdate
                )
                if (response.isSuccessful) {
                    _postStolenReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _postStolenReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _postStolenReport.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _postStolenReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _postStolenReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getPostUploadFileStolen(
        token: String?,
        table_name: String?,
        table_key: String?,
        table_key_id: String?,
        table_column: String?,
        file_dir: String?,
        file_load: MultipartBody.Part?
    ) {
        _postUploadFileStolen.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postUploadStolen(token, table_name, table_key, table_key_id, table_column, file_dir, file_load)
                if (response.isSuccessful) {
                    _postUploadFileStolen.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _postUploadFileStolen.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _postUploadFileStolen.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _postUploadFileStolen.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _postUploadFileStolen.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getStolenReport(token: String?) {
        _stolenReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getStolenReportChart(token)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _stolenReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stolenReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stolenReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stolenReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stolenReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getStolenReportCurrentMonth(token: String?) {
        _stolenReportCurrentMonth.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getStolenReportCurrentMonth(token)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _stolenReportCurrentMonth.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stolenReportCurrentMonth.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stolenReportCurrentMonth.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stolenReportCurrentMonth.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stolenReportCurrentMonth.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getStolenKRIReport(token: String?) {
        _stolenKRIReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getStolenReportKRI(token)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _stolenKRIReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _stolenKRIReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _stolenKRIReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _stolenKRIReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _stolenKRIReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}