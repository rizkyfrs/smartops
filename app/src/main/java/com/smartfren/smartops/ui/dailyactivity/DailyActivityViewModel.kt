package com.smartfren.smartops.ui.dailyactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.dao.SiteInfoDao
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.database.SiteVisitRoomDatabase
import com.smartfren.smartops.entity.SiteInfo
import com.smartfren.smartops.ui.dailyactivity.repository.DailyActivityRepository
import com.smartfren.smartops.util.Event
import com.smartfren.smartops.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DailyActivityViewModel @Inject constructor(
    application: Application,
    private val repository: DailyActivityRepository,
) : AndroidViewModel(application) {
    private var rcaLev: String? = null
    private val _listSchedule = MutableLiveData<Event<Resource<ScheduleResponse>>>()
    private val _listTeamSchedule = MutableLiveData<Event<Resource<TeamListResponse>>>()
    private val _listDateSchedule = MutableLiveData<Event<Resource<WorkingDateResponse>>>()
    private val _listTaskDetailSchedule = MutableLiveData<Event<Resource<ScheduleTaskListDetailResponse>>>()
    private val _activityTaskDetailSchedule = MutableLiveData<Event<Resource<ScheduleTaskDetailResponse>>>()
    private val _listSite = MutableLiveData<Event<Resource<ReferenceSiteResponse>>>()
    private val _actLevel1 = MutableLiveData<Event<Resource<ReferenceDomainLev1Response>>>()
    private val _actLevel2 = MutableLiveData<Event<Resource<ReferenceDomainLev2Response>>>()
    private val _postValidateTask = MutableLiveData<Event<Resource<GeneralResponse>>>()
    private val _postRCA = MutableLiveData<Event<Resource<ListRCAResponse>>>()
    private val _postUpdateTask = MutableLiveData<Event<Resource<GeneralResponse>>>()
    private val _postUpdateTaskManual = MutableLiveData<Event<Resource<GeneralResponse>>>()
    private val _getLiveFLMActivity = MutableLiveData<Event<Resource<LiveFLMActivityListResponse>>>()

    val listSchedule: LiveData<Event<Resource<ScheduleResponse>>> = _listSchedule
    val listTeamSchedule: LiveData<Event<Resource<TeamListResponse>>> = _listTeamSchedule
    val listDateSchedule: LiveData<Event<Resource<WorkingDateResponse>>> = _listDateSchedule
    val listTaskDetailSchedule: LiveData<Event<Resource<ScheduleTaskListDetailResponse>>> = _listTaskDetailSchedule
    val activityTaskDetailSchedule: LiveData<Event<Resource<ScheduleTaskDetailResponse>>> = _activityTaskDetailSchedule
    val siteData: LiveData<Event<Resource<ReferenceSiteResponse>>> = _listSite
    val actLevel1Data: LiveData<Event<Resource<ReferenceDomainLev1Response>>> = _actLevel1
    val actLevel2Data: LiveData<Event<Resource<ReferenceDomainLev2Response>>> = _actLevel2
    val postValidateTaskData: LiveData<Event<Resource<GeneralResponse>>> = _postValidateTask
    val postRCAData: LiveData<Event<Resource<ListRCAResponse>>> = _postRCA
    val postUpdateTaskData: LiveData<Event<Resource<GeneralResponse>>> = _postUpdateTask
    val postUpdateTaskManualData: LiveData<Event<Resource<GeneralResponse>>> = _postUpdateTaskManual
    val getLiveFLMActivity: LiveData<Event<Resource<LiveFLMActivityListResponse>>> = _getLiveFLMActivity

    private val _siteIDSelection = MutableLiveData<String?>()
    val siteIDSelection: LiveData<String?> = _siteIDSelection

    private var daoSiteInfo: SiteInfoDao
    private val context = getApplication<Application>().applicationContext
    private val listSiteInfo : MutableList<SiteInfo> = mutableListOf()

    init {
        val database = SiteVisitRoomDatabase.getDatabase(context)
        daoSiteInfo = database.getSiteInfoDao()
    }

    fun setSiteIDSelection(site_id: String?) {
        _siteIDSelection.value = site_id
    }

    fun setSiteID(): String? {
        val site_id = siteIDSelection.value
        return site_id
    }

    fun getListSchedule(token: String?, sort: String?) = viewModelScope.launch {
        getListSchedulePIC(token, sort)
    }

    fun getListTeamSchedule(token: String?) = viewModelScope.launch {
        getListTeamSchedulePIC(token)
    }

    fun getListDateSchedule(token: String?) = viewModelScope.launch {
        getListDateSchedulePIC(token)
    }

    fun getListTaskDetailSchedule(token: String?, site: String?) = viewModelScope.launch {
        getTaskListDetailSchedule(token, site)
    }

    fun getActivityTaskDetailSchedule(token: String?, site: String?, activityNumber: String?) = viewModelScope.launch {
        getTaskActivityDetailSchedule(token, site, activityNumber)
    }

    fun listSite(token: String, site: String) = viewModelScope.launch {
        getSite(token, site)
    }

    fun listActLevel1(token: String?) = viewModelScope.launch {
        getACTLevel1(token)
    }

    fun listActLevel2(token: String?, domains: String?) = viewModelScope.launch {
        getACTLevel2(token, domains)
    }

    fun postValidateTask(token: String?, taskNum: String?) = viewModelScope.launch {
        postValidate(token, taskNum)
    }

    fun postRCATask(token: String?, rca_level: String?, rca_group: String?, rca_parent: String?) = viewModelScope.launch {
        postRCA(token, rca_level, rca_group, rca_parent)
    }

    fun rcaLevel(): String? {
        return rcaLev
    }

    fun postUpdateTask(
        token: String?, site: String?, activityNumber: String?,
        pic_before: MultipartBody.Part?,
        pic_after: MultipartBody.Part?,
        rca_lev_1: String?,
        rca_lev_2: String?,
        rca_lev_3: String?,
        rca_action: String?,
        action_type: String?,
        action_detail: String?,
        activity_status: String?,
        pending_remarks_1: String?,
        pending_remarks_2: String?,
    ) = viewModelScope.launch {
        postUpdate(
            token,
            site,
            activityNumber,
            pic_before,
            pic_after,
            rca_lev_1,
            rca_lev_2,
            rca_lev_3,
            rca_action,
            action_type,
            action_detail,
            activity_status,
            pending_remarks_1,
            pending_remarks_2
        )
    }

    fun postUpdateTaskManualTask(
        token: String?, site: String?,
        region_code: String?,
        domain_lev_1: String?,
        domain_lev_2: String?,
        site_id: String?,
        ne_name: String?,
        wo_id: String?,
        priority: String?,
        action_needed: String?,
        actual_date: String?,
        pic_before: MultipartBody.Part?,
        pic_after: MultipartBody.Part?,
        rca_lev_1: String?,
        rca_lev_2: String?,
        rca_lev_3: String?,
        rca_action: String?,
        action_type: String?,
        action_detail: String?,
        activity_status: String?,
        pending_remarks_1: String?,
        pending_remarks_2: String?,
        action_remark: String?,
    ) = viewModelScope.launch {
        postUpdateTaskManual(
            token, site,
            region_code,
            domain_lev_1,
            domain_lev_2,
            site_id,
            ne_name,
            wo_id,
            priority,
            action_needed,
            actual_date,
            pic_before, pic_after,
            rca_lev_1,
            rca_lev_2,
            rca_lev_3,
            rca_action,
            action_type,
            action_detail,
            activity_status,
            pending_remarks_1,
            pending_remarks_2,
            action_remark
        )
    }

    fun setListFLMActivity(start: String?, recperpage: String?) = viewModelScope.launch {
        getListFLMActivity(start, recperpage)
    }

    private suspend fun getListSchedulePIC(token: String?, sort: String?) {
        _listSchedule.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListSchedule(token, sort)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listSchedule.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listSchedule.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listSchedule.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listSchedule.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listSchedule.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListTeamSchedulePIC(token: String?) {
        _listTeamSchedule.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getTeamListSchedule(token)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _listTeamSchedule.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listSchedule.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listSchedule.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listSchedule.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listSchedule.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListDateSchedulePIC(token: String?) {
        _listDateSchedule.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getDateListSchedule(token)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _listDateSchedule.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listDateSchedule.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listDateSchedule.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listDateSchedule.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listDateSchedule.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTaskListDetailSchedule(token: String?, site: String?) {
        _listTaskDetailSchedule.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListDetailSchedule(token, site)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _listTaskDetailSchedule.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listTaskDetailSchedule.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listTaskDetailSchedule.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listTaskDetailSchedule.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listTaskDetailSchedule.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getTaskActivityDetailSchedule(token: String?, site: String?, activityNumber: String?) {
        _activityTaskDetailSchedule.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getActivityDetailSchedule(token, site, activityNumber)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _activityTaskDetailSchedule.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _activityTaskDetailSchedule.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _activityTaskDetailSchedule.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _activityTaskDetailSchedule.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _activityTaskDetailSchedule.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getSite(token: String?, site: String?) {
        _listSite.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSite(token, site)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    } else {
                        delete()
                        for (sites in response.body()?.getReport()?.sites!!) {
                            insertSiteIfo(SiteInfo(site_info = sites.siteId))
                        }
                    }
                    _listSite.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listSite.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listSite.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listSite.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listSite.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getACTLevel1(token: String?) {
        _actLevel1.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getActLevel1(token)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _actLevel1.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _actLevel1.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _actLevel1.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _actLevel1.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _actLevel1.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getACTLevel2(token: String?, domains: String?) {
        _actLevel2.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getActLevel2(token, domains)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _actLevel2.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _actLevel2.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _actLevel2.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _actLevel2.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _actLevel2.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postValidate(token: String?, taskNum: String?) {
        _postValidateTask.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postValidateTask(token, taskNum)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _postValidateTask.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _postValidateTask.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _postValidateTask.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _postValidateTask.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _postValidateTask.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postRCA(token: String?, rca_level: String?, rca_group: String?, rca_parent: String?) {
        _postRCA.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postRCATask(token, rca_level, rca_group, rca_parent)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    rcaLev = rca_level
                    _postRCA.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _postRCA.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _postRCA.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _postRCA.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _postRCA.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postUpdate(
        token: String?, site: String?, activityNumber: String?,
        pic_before: MultipartBody.Part?,
        pic_after: MultipartBody.Part?,
        rca_lev_1: String?,
        rca_lev_2: String?,
        rca_lev_3: String?,
        rca_action: String?,
        action_type: String?,
        action_detail: String?,
        activity_status: String?,
        pending_remarks_1: String?,
        pending_remarks_2: String?,
    ) {
        _postUpdateTask.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postUpdateTask(
                    token,
                    site,
                    activityNumber,
                    pic_before,
                    pic_after,
                    rca_lev_1,
                    rca_lev_2,
                    rca_lev_3,
                    rca_action,
                    action_type,
                    action_detail,
                    activity_status,
                    pending_remarks_1,
                    pending_remarks_2
                )
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _postUpdateTask.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _postUpdateTask.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _postUpdateTask.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _postUpdateTask.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _postUpdateTask.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postUpdateTaskManual(
        token: String?, site: String?,
        region_code: String?,
        domain_lev_1: String?,
        domain_lev_2: String?,
        site_id: String?,
        ne_name: String?,
        wo_id: String?,
        priority: String?,
        action_needed: String?,
        actual_date: String?,
        pic_before: MultipartBody.Part?,
        pic_after: MultipartBody.Part?,
        rca_lev_1: String?,
        rca_lev_2: String?,
        rca_lev_3: String?,
        rca_action: String?,
        action_type: String?,
        action_detail: String?,
        activity_status: String?,
        pending_remarks_1: String?,
        pending_remarks_2: String?,
        action_remark: String?,
    ) {
        _postUpdateTaskManual.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postUpdateTaskManual(
                    token, site,
                    region_code,
                    domain_lev_1,
                    domain_lev_2,
                    site_id,
                    ne_name,
                    wo_id,
                    priority,
                    action_needed,
                    actual_date,
                    pic_before, pic_after,
                    rca_lev_1,
                    rca_lev_2,
                    rca_lev_3,
                    rca_action,
                    action_type,
                    action_detail,
                    activity_status,
                    pending_remarks_1,
                    pending_remarks_2,
                    action_remark
                )
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _postUpdateTaskManual.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _postUpdateTaskManual.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _postUpdateTaskManual.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _postUpdateTaskManual.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _postUpdateTaskManual.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getListFLMActivity(start: String?, recperpage: String?) {
        _getLiveFLMActivity.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getLiveFLMActivity(start, recperpage)
                if (response.isSuccessful) {
                    _getLiveFLMActivity.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _getLiveFLMActivity.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _getLiveFLMActivity.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _getLiveFLMActivity.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _getLiveFLMActivity.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    fun insertSiteIfo(siteinfo: SiteInfo) {
        daoSiteInfo.insert(siteinfo)
    }

    fun delete() {
        viewModelScope.launch(Dispatchers.IO) {
            daoSiteInfo.delete()
        }
    }

    fun getSiteInfo(): MutableList<String> {
        return daoSiteInfo.getAll()
    }
}