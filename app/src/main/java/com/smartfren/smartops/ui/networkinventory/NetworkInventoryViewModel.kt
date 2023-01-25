package com.smartfren.smartops.ui.networkinventory

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.networkinventory.repository.NetworkInventoryRepository
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
class NetworkInventoryViewModel @Inject constructor(
    application: Application,
    private val repository: NetworkInventoryRepository,
) : AndroidViewModel(application) {
    private val _siteInfoByVendorData = MutableLiveData<Event<Resource<SiteInfoByVendorSumResponse>>>()
    val siteInfoByVendorData: LiveData<Event<Resource<SiteInfoByVendorSumResponse>>> = _siteInfoByVendorData

    private val _siteInfoBySiteData = MutableLiveData<Event<Resource<SiteInfoBySiteSumResponse>>>()
    val siteInfoBySiteData: LiveData<Event<Resource<SiteInfoBySiteSumResponse>>> = _siteInfoBySiteData

    private val _siteInfoByFONodeData = MutableLiveData<Event<Resource<SiteInfoByFONodeSumResponse>>>()
    val siteInfoByFONodeData: LiveData<Event<Resource<SiteInfoByFONodeSumResponse>>> = _siteInfoByFONodeData

    private val _siteInfoByModelData = MutableLiveData<Event<Resource<SiteInfoBySiteModelSumResponse>>>()
    val siteInfoByModelData: LiveData<Event<Resource<SiteInfoBySiteModelSumResponse>>> = _siteInfoByModelData

    private val _siteInfoByFGSData = MutableLiveData<Event<Resource<SiteInfoByFGSSumResponse>>>()
    val siteInfoByFGSData: LiveData<Event<Resource<SiteInfoByFGSSumResponse>>> = _siteInfoByFGSData

    private val _siteInfoByTopologyData = MutableLiveData<Event<Resource<SiteInfoBySiteTopologySumResponse>>>()
    val siteInfoByTopologyData: LiveData<Event<Resource<SiteInfoBySiteTopologySumResponse>>> = _siteInfoByTopologyData

    private val _clusteringReport = MutableLiveData<Event<Resource<ClusteringReportResponse>>>()
    val clusteringReport: LiveData<Event<Resource<ClusteringReportResponse>>> = _clusteringReport

    private val _teamAvailabilityReport = MutableLiveData<Event<Resource<TeamAvailabilityResponse>>>()
    val teamAvailabilityReport: LiveData<Event<Resource<TeamAvailabilityResponse>>> = _teamAvailabilityReport

    private val _teamAvailabilityTechReport = MutableLiveData<Event<Resource<TeamAvailabilityResponse>>>()
    val teamAvailabilityTechReport: LiveData<Event<Resource<TeamAvailabilityResponse>>> = _teamAvailabilityTechReport

    private val _smartopsLoginReport = MutableLiveData<Event<Resource<SmartopsLoginReportResponse>>>()
    val smartopsLoginReport: LiveData<Event<Resource<SmartopsLoginReportResponse>>> = _smartopsLoginReport

    private val _smartopsLoginTechReport = MutableLiveData<Event<Resource<SmartopsLoginReportResponse>>>()
    val smartopsLoginTechReport: LiveData<Event<Resource<SmartopsLoginReportResponse>>> = _smartopsLoginTechReport

    private val _mdmStatusFLMReport = MutableLiveData<Event<Resource<MDMStatusFLMResponse>>>()
    val mdmStatusFLMReport: LiveData<Event<Resource<MDMStatusFLMResponse>>> = _mdmStatusFLMReport

    private val _mdmStatusTechReport = MutableLiveData<Event<Resource<MDMStatusFLMResponse>>>()
    val mdmStatusTechReport: LiveData<Event<Resource<MDMStatusFLMResponse>>> = _mdmStatusTechReport

    fun siteInfoBySite() = viewModelScope.launch {
        getSiteInfoBySite()
    }

    fun siteInfoByVendor() = viewModelScope.launch {
        getSiteInfoByVendor()
    }

    fun setSiteInfoFONode() = viewModelScope.launch {
        getSiteInfoByFONode()
    }

    fun setSiteInfoByModel() = viewModelScope.launch {
        getSiteInfoByModel()
    }

    fun siteInfoByFGS() = viewModelScope.launch {
        getSiteInfoByFGS()
    }

    fun siteInfoByTopology() = viewModelScope.launch {
        getSiteInfoByTopology()
    }

    fun setClusterReport(token: String?) = viewModelScope.launch {
        getClusterReport(token)
    }

    fun setTeamAvailabilityReport(token: String?, id: String?) = viewModelScope.launch {
        getTeamAvailabilityReport(token, id)
    }

    fun setTeamAvailabilityTechReport(token: String?, id: String?) = viewModelScope.launch {
        getTeamAvailabilityTechReport(token, id)
    }

    fun setSmartopsLoginReport(token: String?, id: String?) = viewModelScope.launch {
        getSmartopsLoginReport(token, id)
    }

    fun setSmartopsLoginTechReport(token: String?, id: String?) = viewModelScope.launch {
        getSmartopsLoginTechReport(token, id)
    }

    fun setMDMStatusFLMReport(token: String?, id: String?) = viewModelScope.launch {
        getMDMStatusFLMReport(token, id)
    }

    fun setMDMStatusTechReport(token: String?, id: String?) = viewModelScope.launch {
        getMDMStatusTechReport(token, id)
    }

    private suspend fun getSiteInfoBySite() {
        _siteInfoBySiteData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteInfoBySite()
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _siteInfoBySiteData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteInfoBySiteData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteInfoBySiteData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteInfoBySiteData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteInfoBySiteData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSiteInfoByVendor() {
        _siteInfoByVendorData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteInfoByVendor()
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _siteInfoByVendorData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteInfoByVendorData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteInfoByVendorData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteInfoByVendorData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteInfoByVendorData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSiteInfoByModel() {
        _siteInfoByModelData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteInfoByModel()
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _siteInfoByModelData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteInfoByModelData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteInfoByModelData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteInfoByModelData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteInfoByModelData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSiteInfoByFONode() {
        _siteInfoByFONodeData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteInfoByByFONode()
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _siteInfoByFONodeData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteInfoByFONodeData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteInfoByFONodeData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteInfoByFONodeData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteInfoByFONodeData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSiteInfoByFGS() {
        _siteInfoByFGSData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteInfoByFGS()
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _siteInfoByFGSData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteInfoByFGSData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteInfoByFGSData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteInfoByFGSData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteInfoByFGSData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSiteInfoByTopology() {
        _siteInfoByTopologyData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteInfoByTopology()
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _siteInfoByTopologyData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _siteInfoByTopologyData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _siteInfoByTopologyData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _siteInfoByTopologyData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _siteInfoByTopologyData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getClusterReport(token: String?) {
        _clusteringReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getClusterReport(token)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _clusteringReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _clusteringReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _clusteringReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _clusteringReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _clusteringReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTeamAvailabilityReport(token: String?, id: String?) {
        _teamAvailabilityReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTeamAvailabilityReport(token, id)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _teamAvailabilityReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _teamAvailabilityReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _teamAvailabilityReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _teamAvailabilityReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _teamAvailabilityReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTeamAvailabilityTechReport(token: String?, id: String?) {
        _teamAvailabilityTechReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTeamAvailabilityReport(token, id)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _teamAvailabilityTechReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _teamAvailabilityTechReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _teamAvailabilityTechReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _teamAvailabilityTechReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _teamAvailabilityTechReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSmartopsLoginReport(token: String?, id: String?) {
        _smartopsLoginReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSmartopsLoginReport(token, id)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _smartopsLoginReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _smartopsLoginReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _smartopsLoginReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _smartopsLoginReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _smartopsLoginReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSmartopsLoginTechReport(token: String?, id: String?) {
        _smartopsLoginTechReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSmartopsLoginReport(token, id)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _smartopsLoginTechReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _smartopsLoginTechReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _smartopsLoginTechReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _smartopsLoginTechReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _smartopsLoginTechReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getMDMStatusFLMReport(token: String?, id: String?) {
        _mdmStatusFLMReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getMDMStatusFLMReport(token, id)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _mdmStatusFLMReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _mdmStatusFLMReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _mdmStatusFLMReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _mdmStatusFLMReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _mdmStatusFLMReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getMDMStatusTechReport(token: String?, id: String?) {
        _mdmStatusTechReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getMDMStatusFLMReport(token, id)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _mdmStatusTechReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _mdmStatusTechReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _mdmStatusTechReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _mdmStatusTechReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _mdmStatusTechReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}