package com.smartfren.smartops.ui.tracker

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
class TrackerViewModel @Inject constructor(
    application: Application,
    private val repository: TrackerRepository,
) : AndroidViewModel(application) {
    private val _trackerFGSData = MutableLiveData<Event<Resource<TrackerFGSResponse>>>()
    val trackerFGSData: LiveData<Event<Resource<TrackerFGSResponse>>> = _trackerFGSData

    private val _trackerBatteryData = MutableLiveData<Event<Resource<TrackerBatteryResponse>>>()
    val trackerBatteryData: LiveData<Event<Resource<TrackerBatteryResponse>>> = _trackerBatteryData

    private val _trackerACData = MutableLiveData<Event<Resource<TrackerACResponse>>>()
    val trackerACData: LiveData<Event<Resource<TrackerACResponse>>> = _trackerACData

    private val _trackerGroundingData = MutableLiveData<Event<Resource<TrackerGroundingResponse>>>()
    val trackerGroundingData: LiveData<Event<Resource<TrackerGroundingResponse>>> = _trackerGroundingData

    private val _trackerLandscapeData = MutableLiveData<Event<Resource<TrackerLandscapeResponse>>>()
    val trackerLandscapeData: LiveData<Event<Resource<TrackerLandscapeResponse>>> = _trackerLandscapeData

    private val _trackerAccessibilityData = MutableLiveData<Event<Resource<TrackerAccessibilityResponse>>>()
    val trackerAccessibilityData: LiveData<Event<Resource<TrackerAccessibilityResponse>>> = _trackerAccessibilityData

    private val _trackerAccessibilityOpenData = MutableLiveData<Event<Resource<TrackerAccessibilityOpenResponse>>>()
    val trackerAccessibilityOpenData: LiveData<Event<Resource<TrackerAccessibilityOpenResponse>>> = _trackerAccessibilityOpenData

    private val _trackerTravellingData = MutableLiveData<Event<Resource<TrackerTravellingResponse>>>()
    val trackerTravellingData: LiveData<Event<Resource<TrackerTravellingResponse>>> = _trackerTravellingData

    private val _trackerIMBData = MutableLiveData<Event<Resource<TrackerIMBResponse>>>()
    val trackerIMBData: LiveData<Event<Resource<TrackerIMBResponse>>> = _trackerIMBData

    private val _trackerLowFuelData = MutableLiveData<Event<Resource<TrackerLowFuelResponse>>>()
    val trackerLowFuelData: LiveData<Event<Resource<TrackerLowFuelResponse>>> = _trackerLowFuelData

    private val _trackerProblematicData = MutableLiveData<Event<Resource<TrackerProblematicResponse>>>()
    val trackerProblematicData: LiveData<Event<Resource<TrackerProblematicResponse>>> = _trackerProblematicData

    private val _trackerWorstCellData = MutableLiveData<Event<Resource<TrackerWorstCellResponse>>>()
    val trackerWorstCellData: LiveData<Event<Resource<TrackerWorstCellResponse>>> = _trackerWorstCellData

    private val _trackerNWeeksData = MutableLiveData<Event<Resource<TrackerNWeeksResponse>>>()
    val trackerNWeeksData: LiveData<Event<Resource<TrackerNWeeksResponse>>> = _trackerNWeeksData

    private val _trackerRiskListData = MutableLiveData<Event<Resource<TrackerRiskListResponse>>>()
    val trackerRiskListData: LiveData<Event<Resource<TrackerRiskListResponse>>> = _trackerRiskListData

    private val _trackerRiskListGHData = MutableLiveData<Event<Resource<TrackerRiskGHListResponse>>>()
    val trackerRiskListGHData: LiveData<Event<Resource<TrackerRiskGHListResponse>>> = _trackerRiskListGHData

    private val _trackerRiskViewData = MutableLiveData<Event<Resource<TrackerRiskViewResponse>>>()
    val trackerRiskViewData: LiveData<Event<Resource<TrackerRiskViewResponse>>> = _trackerRiskViewData

    private val _trackerRiskSTOMViewData = MutableLiveData<Event<Resource<TrackerRiskSTOMViewResponse>>>()
    val trackerRiskSTOMViewData: LiveData<Event<Resource<TrackerRiskSTOMViewResponse>>> = _trackerRiskSTOMViewData

    private val _trackerRiskGroupLv1Data = MutableLiveData<Event<Resource<TrackerGroupLevel1Response>>>()
    val trackerRiskGroupLv1Data: LiveData<Event<Resource<TrackerGroupLevel1Response>>> = _trackerRiskGroupLv1Data

    private val _trackerRiskGroupLv2Data = MutableLiveData<Event<Resource<TrackerGroupLevel2Response>>>()
    val trackerRiskGroupLv2Data: LiveData<Event<Resource<TrackerGroupLevel2Response>>> = _trackerRiskGroupLv2Data

    private val _addRiskTrackerData = MutableLiveData<Event<Resource<TrackerRiskAddResponse>>>()
    val addRiskTrackerData: LiveData<Event<Resource<TrackerRiskAddResponse>>> = _addRiskTrackerData

    private val _editRiskTrackerData = MutableLiveData<Event<Resource<TrackerRiskAddResponse>>>()
    val editRiskTrackerData: LiveData<Event<Resource<TrackerRiskAddResponse>>> = _editRiskTrackerData

    private val _addRiskTrackerSTOMData = MutableLiveData<Event<Resource<TrackerRiskAddResponse>>>()
    val addRiskTrackerSTOMData: LiveData<Event<Resource<TrackerRiskAddResponse>>> = _addRiskTrackerSTOMData

    private val _editRiskTrackerSTOMData = MutableLiveData<Event<Resource<TrackerRiskAddResponse>>>()
    val editRiskTrackerSTOMData: LiveData<Event<Resource<TrackerRiskAddResponse>>> = _editRiskTrackerSTOMData

    private val _updateRiskTrackerGHData = MutableLiveData<Event<Resource<TrackerRiskUpdateGHResponse>>>()
    val updateRiskTrackerGHData: LiveData<Event<Resource<TrackerRiskUpdateGHResponse>>> = _updateRiskTrackerGHData

    private val _trackerRiskListSTOMData = MutableLiveData<Event<Resource<TrackerRiskListSTOMResponse>>>()
    val trackerRiskListSTOMData: LiveData<Event<Resource<TrackerRiskListSTOMResponse>>> = _trackerRiskListSTOMData

    private val _trackerManagerData = MutableLiveData<Event<Resource<TrackerManagerResponse>>>()
    val trackerManagerData: LiveData<Event<Resource<TrackerManagerResponse>>> = _trackerManagerData

    private val _trackerManagerViewData = MutableLiveData<Event<Resource<TrackerManagerViewResponse>>>()
    val trackerManagerViewData: LiveData<Event<Resource<TrackerManagerViewResponse>>> = _trackerManagerViewData

    fun trackerFGSDataList() = viewModelScope.launch {
        getTrackerFGS()
    }

    fun trackerBatteryDataList() = viewModelScope.launch {
        getTrackerBattery()
    }

    fun trackerACDataList() = viewModelScope.launch {
        getTrackerAC()
    }

    fun trackerGroundingDataList() = viewModelScope.launch {
        getTrackerGrounding()
    }

    fun trackerLandscapeDataList() = viewModelScope.launch {
        getTrackerLandscape()
    }

    fun trackerAccessibilityDataList() = viewModelScope.launch {
        getTrackerAccessibility()
    }

    fun trackerAccessibilityOpenDataList() = viewModelScope.launch {
        getTrackerAccessibilityOpen()
    }

    fun trackerTravellingDataList() = viewModelScope.launch {
        getTrackerTravelling()
    }

    fun trackerIMBDataList() = viewModelScope.launch {
        getTrackerIMB()
    }

    fun trackerLowFuelDataList() = viewModelScope.launch {
        getTrackerLowFuel()
    }

    fun trackerProblematicDataList() = viewModelScope.launch {
        getTrackerProblematic()
    }

    fun trackerWorstCellDataList() = viewModelScope.launch {
        getTrackerWorstCell()
    }

    fun trackerNWeeksDataList() = viewModelScope.launch {
        getTrackerNWeeks()
    }

    fun setTrackerRisk(start: String?, recperpage: String?, status: String?) =
        viewModelScope.launch {
            getTrackerRiskList(start, recperpage, status)
        }

    fun setTrackerRiskGH(start: String?, recperpage: String?, status: String?) =
        viewModelScope.launch {
            getTrackerRiskListGH(start, recperpage, status)
        }

    fun setTrackerRiskSTOM(
        start: String?, recperpage: String?, status: String?
    ) = viewModelScope.launch {
        getTrackerRiskSTOMList(start, recperpage, status)
    }

    fun setTrackerManager(
        start: String?, recperpage: String?, status: String?
    ) = viewModelScope.launch {
        getTrackerManagerList(start, recperpage, status)
    }

    fun setTrackerRiskSearch(site_id: String?) = viewModelScope.launch {
        getTrackerRiskSearchList(site_id)
    }

    fun setTrackerRiskView(key: String?) = viewModelScope.launch {
        getTrackerRiskView(key)
    }

    fun setTrackerRiskSTOMView(key: String?) = viewModelScope.launch {
        getTrackerRiskSTOMView(key)
    }

    fun setTrackerManagerView(key: String?) = viewModelScope.launch {
        getTrackerManagerView(key)
    }

    fun setTrackerRiskGroupLv1(key: String?) = viewModelScope.launch {
        getTrackerRiskGroupLv1(key)
    }

    fun setTrackerRiskGroupLv2(key: String?) = viewModelScope.launch {
        getTrackerRiskGroupLv2(key)
    }

    fun setPostAddTrackerRisk(
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?
    ) = viewModelScope.launch {
        postAddTrackerRisk(
            tt_group_level_1, tt_group_level_2, region, site_id, desc,
            pic, action_desc, report_date, target_date, tt_status, created_by,
            created_on
        )
    }

    fun setPostAddTrackerRiskSTOM(
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?
    ) = viewModelScope.launch {
        postAddTrackerRiskSTOM(
            tt_group_level_1, tt_group_level_2, region, site_id, desc,
            pic, action_desc, report_date, target_date, tt_status, created_by,
            created_on
        )
    }

    fun setPostEditTrackerRisk(
        key: String?,
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        user_complaint: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        resolve_date: String?,
        action_taken: String?,
        root_cause_desc: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?
    ) = viewModelScope.launch {
        postEditTrackerRisk(
            key, tt_group_level_1, tt_group_level_2, region, site_id, desc, user_complaint,
            pic, action_desc, report_date, target_date, resolve_date, action_taken, root_cause_desc,
            tt_status, created_by, created_on
        )
    }

    fun setPostEditTrackerRiskSTOM(
        key: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        target_date: String?,
        resolve_date: String?,
        action_taken: String?,
        root_cause_desc: String?,
        tt_status: String?,
        update_by: String?,
        update_on: String?
    ) = viewModelScope.launch {
        postEditTrackerRiskSTOM(
            key, site_id, desc, pic, action_desc, target_date, resolve_date, action_taken,
            root_cause_desc, tt_status, update_by, update_on
        )
    }

    fun setPostUpdateTrackerRiskGH(
        key: String?, close_date: String?, action_taken: String?, root_cause_desc: String?,
        tt_status: String?
    ) = viewModelScope.launch {
        postUpdateTrackerRiskGH(
            key, close_date, action_taken, root_cause_desc, tt_status
        )
    }

    private suspend fun getTrackerFGS() {
        _trackerFGSData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerFGS()
                if (response.isSuccessful) {
                    _trackerFGSData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerFGSData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerFGSData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerFGSData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerFGSData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerAC() {
        _trackerACData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerAC()
                if (response.isSuccessful) {
                    _trackerACData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerACData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerACData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerACData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerACData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerBattery() {
        _trackerBatteryData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerBattery()
                if (response.isSuccessful) {
                    _trackerBatteryData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerBatteryData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerBatteryData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerBatteryData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerBatteryData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerGrounding() {
        _trackerGroundingData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerGrounding()
                if (response.isSuccessful) {
                    _trackerGroundingData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerGroundingData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerGroundingData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerGroundingData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerGroundingData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerLandscape() {
        _trackerLandscapeData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerLandscape()
                if (response.isSuccessful) {
                    _trackerLandscapeData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerLandscapeData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerLandscapeData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerLandscapeData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerLandscapeData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerAccessibility() {
        _trackerAccessibilityData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerAccessibility()
                if (response.isSuccessful) {
                    _trackerAccessibilityData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerAccessibilityData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerAccessibilityData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerAccessibilityData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerAccessibilityData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerAccessibilityOpen() {
        _trackerAccessibilityOpenData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerAccessibilityOpen()
                if (response.isSuccessful) {
                    _trackerAccessibilityOpenData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerAccessibilityOpenData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerAccessibilityOpenData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerAccessibilityOpenData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerAccessibilityOpenData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerTravelling() {
        _trackerTravellingData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerTravelling()
                if (response.isSuccessful) {
                    _trackerTravellingData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerTravellingData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerTravellingData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerTravellingData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerTravellingData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerIMB() {
        _trackerIMBData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerIMB()
                if (response.isSuccessful) {
                    _trackerIMBData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerIMBData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerIMBData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerIMBData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerIMBData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerLowFuel() {
        _trackerLowFuelData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerLowFuel()
                if (response.isSuccessful) {
                    _trackerLowFuelData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerLowFuelData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerLowFuelData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerLowFuelData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerLowFuelData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerProblematic() {
        _trackerProblematicData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerProblematic()
                if (response.isSuccessful) {
                    _trackerProblematicData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerProblematicData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerProblematicData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerProblematicData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerProblematicData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerWorstCell() {
        _trackerWorstCellData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerWorstCell()
                if (response.isSuccessful) {
                    _trackerWorstCellData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerWorstCellData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerWorstCellData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerWorstCellData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerWorstCellData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerNWeeks() {
        _trackerNWeeksData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerNWeeks()
                if (response.isSuccessful) {
                    _trackerNWeeksData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerNWeeksData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerNWeeksData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerNWeeksData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerNWeeksData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerRiskList(
        start: String?, recperpage: String?, status: String?
    ) {
        _trackerRiskListData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerRiskList(start, recperpage, status)
                if (response.isSuccessful) {
                    _trackerRiskListData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerRiskListData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerRiskListData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerRiskListData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerRiskListData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerRiskListGH(
        start: String?, recperpage: String?, status: String?
    ) {
        _trackerRiskListGHData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerRiskGHList(start, recperpage, status)
                if (response.isSuccessful) {
                    _trackerRiskListGHData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerRiskListGHData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerRiskListGHData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerRiskListGHData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerRiskListGHData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerRiskSTOMList(
        start: String?, recperpage: String?, status: String?
    ) {
        _trackerRiskListSTOMData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerRiskSTOMList(start, recperpage, status)
                if (response.isSuccessful) {
                    _trackerRiskListSTOMData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerRiskListSTOMData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerRiskListSTOMData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerRiskListSTOMData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerRiskListSTOMData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerManagerList(
        start: String?, recperpage: String?, status: String?
    ) {
        _trackerManagerData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerManagerList(start, recperpage, status)
                if (response.isSuccessful) {
                    _trackerManagerData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerManagerData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerManagerData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerManagerData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerManagerData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerRiskSearchList(site_id: String?) {
        _trackerRiskListData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerRiskSearchList(site_id)
                if (response.isSuccessful) {
                    _trackerRiskListData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerRiskListData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerRiskListData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerRiskListData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerRiskListData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerRiskView(key: String?) {
        _trackerRiskViewData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerRiskView(key)
                if (response.isSuccessful) {
                    _trackerRiskViewData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerRiskViewData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerRiskViewData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerRiskViewData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerRiskViewData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerRiskSTOMView(key: String?) {
        _trackerRiskSTOMViewData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerRiskSTOMView(key)
                if (response.isSuccessful) {
                    _trackerRiskSTOMViewData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerRiskSTOMViewData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerRiskSTOMViewData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerRiskSTOMViewData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerRiskSTOMViewData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerManagerView(key: String?) {
        _trackerManagerViewData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerManagerView(key)
                if (response.isSuccessful) {
                    _trackerManagerViewData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerManagerViewData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerManagerViewData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerManagerViewData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerManagerViewData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerRiskGroupLv1(key: String?) {
        _trackerRiskGroupLv1Data.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerGroupLv1(key)
                if (response.isSuccessful) {
                    _trackerRiskGroupLv1Data.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerRiskGroupLv1Data.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerRiskGroupLv1Data.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerRiskGroupLv1Data.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerRiskGroupLv1Data.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTrackerRiskGroupLv2(key: String?) {
        _trackerRiskGroupLv2Data.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTrackerGroupLv2(key)
                if (response.isSuccessful) {
                    _trackerRiskGroupLv2Data.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _trackerRiskGroupLv2Data.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _trackerRiskGroupLv2Data.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _trackerRiskGroupLv2Data.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _trackerRiskGroupLv2Data.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postAddTrackerRisk(
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?,
    ) {
        _addRiskTrackerData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postAddRiskTracker(
                    tt_group_level_1, tt_group_level_2, region, site_id, desc,
                    pic, action_desc, report_date, target_date, tt_status, created_by,
                    created_on
                )
                if (response.isSuccessful) {
                    _addRiskTrackerData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _addRiskTrackerData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _addRiskTrackerData.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _addRiskTrackerData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _addRiskTrackerData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postAddTrackerRiskSTOM(
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?,
    ) {
        _addRiskTrackerSTOMData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postAddRiskTrackerSTOM(
                    tt_group_level_1, tt_group_level_2, region, site_id, desc,
                    pic, action_desc, report_date, target_date, tt_status, created_by,
                    created_on
                )
                if (response.isSuccessful) {
                    _addRiskTrackerSTOMData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _addRiskTrackerSTOMData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _addRiskTrackerSTOMData.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _addRiskTrackerSTOMData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _addRiskTrackerSTOMData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postEditTrackerRisk(
        key: String?,
        tt_group_level_1: String?,
        tt_group_level_2: String?,
        region: String?,
        site_id: String?,
        desc: String?,
        user_complaint: String?,
        pic: String?,
        action_desc: String?,
        report_date: String?,
        target_date: String?,
        resolve_date: String?,
        action_taken: String?,
        root_cause_desc: String?,
        tt_status: String?,
        created_by: String?,
        created_on: String?
    ) {
        _editRiskTrackerData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postEditRiskTracker(
                    key,
                    tt_group_level_1,
                    tt_group_level_2,
                    region,
                    site_id,
                    desc,
                    user_complaint,
                    pic,
                    action_desc,
                    report_date,
                    target_date,
                    resolve_date,
                    action_taken,
                    root_cause_desc,
                    tt_status,
                    created_by,
                    created_on
                )
                if (response.isSuccessful) {
                    _editRiskTrackerData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _editRiskTrackerData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _editRiskTrackerData.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _editRiskTrackerData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _editRiskTrackerData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postEditTrackerRiskSTOM(
        key: String?,
        site_id: String?,
        desc: String?,
        pic: String?,
        action_desc: String?,
        target_date: String?,
        resolve_date: String?,
        action_taken: String?,
        root_cause_desc: String?,
        tt_status: String?,
        update_by: String?,
        update_on: String?
    ) {
        _editRiskTrackerSTOMData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postEditRiskTrackerSTOM(
                    key, site_id, desc, pic, action_desc, target_date, resolve_date, action_taken,
                    root_cause_desc, tt_status, update_by, update_on
                )
                if (response.isSuccessful) {
                    _editRiskTrackerSTOMData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _editRiskTrackerSTOMData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _editRiskTrackerSTOMData.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _editRiskTrackerSTOMData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _editRiskTrackerSTOMData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postUpdateTrackerRiskGH(
        key: String?, close_date: String?, action_taken: String?, root_cause_desc: String?,
        tt_status: String?
    ) {
        _updateRiskTrackerGHData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postUpdateRiskGHTracker(
                    key, close_date, action_taken, root_cause_desc, tt_status
                )
                if (response.isSuccessful) {
                    _updateRiskTrackerGHData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _updateRiskTrackerGHData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _updateRiskTrackerGHData.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _updateRiskTrackerGHData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _updateRiskTrackerGHData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}