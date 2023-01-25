package com.smartfren.smartops.ui.reportactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.*
import com.smartfren.smartops.ui.reportactivity.repository.ActivityReportRepository
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
class ActivityReportViewModel @Inject constructor(
    application: Application,
    private val repository: ActivityReportRepository,
) : AndroidViewModel(application) {
    private val _listActivityReport = MutableLiveData<Event<Resource<ActivityReportResponse>>>()
    private val _listActivityChecklist =
        MutableLiveData<Event<Resource<ActivityChecklistResponse>>>()
    private val _listFormGroup = MutableLiveData<Event<Resource<ActivityReportGroupResponse>>>()
    private val _listFormGroupType =
        MutableLiveData<Event<Resource<ActivityReportGroupTypeResponse>>>()
    private val _listSite = MutableLiveData<Event<Resource<ReferenceSiteResponse>>>()
    private val _listChecklistID = MutableLiveData<Event<Resource<ActivityChecklistIDResponse>>>()
    private val _postActivityReportForm = MutableLiveData<Event<Resource<GeneralResponse>>>()
    private val _postActivityChecklistForm = MutableLiveData<Event<Resource<GeneralResponse>>>()

    val activityReportData: LiveData<Event<Resource<ActivityReportResponse>>> = _listActivityReport
    val activityChecklistData: LiveData<Event<Resource<ActivityChecklistResponse>>> =
        _listActivityChecklist
    val formGroupData: LiveData<Event<Resource<ActivityReportGroupResponse>>> = _listFormGroup
    val formGroupTypeData: LiveData<Event<Resource<ActivityReportGroupTypeResponse>>> =
        _listFormGroupType
    val siteData: LiveData<Event<Resource<ReferenceSiteResponse>>> = _listSite
    val checklistIDData: LiveData<Event<Resource<ActivityChecklistIDResponse>>> = _listChecklistID
    val activityReportForm: LiveData<Event<Resource<GeneralResponse>>> = _postActivityReportForm
    val activityChecklistForm: LiveData<Event<Resource<GeneralResponse>>> =
        _postActivityChecklistForm

    private val _postActivityReportFormNew = MutableLiveData<Event<Resource<GeneralResponse>>>()
    val postActivityReportFormNew: LiveData<Event<Resource<GeneralResponse>>> =
        _postActivityReportFormNew

    private val _listChecklistAdditional = MutableLiveData<Event<Resource<ChecklistItemAdditionalResponse>>>()
    val listChecklistAdditional: LiveData<Event<Resource<ChecklistItemAdditionalResponse>>> =
        _listChecklistAdditional

    fun listActivityReport(token: String?, page: String?) = viewModelScope.launch {
        getListActivityReport(token, page)
    }

    fun listActivityChecklist(token: String?, formNum: String?) = viewModelScope.launch {
        getActivityChecklist(token, formNum)
    }

    fun listFormGroup(token: String?) = viewModelScope.launch {
        getFormGroup(token)
    }

    fun listFormGroupType(token: String?, form_group: String?) = viewModelScope.launch {
        getFormGroupType(token, form_group)
    }

    fun listSite(token: String, site: String) = viewModelScope.launch {
        getSite(token, site)
    }

    fun listCheckListID(token: String?, checklistID: String?) = viewModelScope.launch {
        getChecklistID(token, checklistID)
    }

    fun listActivityChecklistAdditional(token: String?, formNum: String?) = viewModelScope.launch {
        getChecklistAdditional(token, formNum)
    }

    fun postForm(
        token: String?,
        act_theme_id: String?,
        act_region: String?,
        act_site_id: String?,
        act_date: String?,
        act_avail: String?,
        act_notes: String?
    ) = viewModelScope.launch {
        postActivityReportForm(
            token,
            act_theme_id,
            act_region,
            act_site_id,
            act_date,
            act_avail,
            act_notes
        )

    }

    fun postChecklistForm(
        token: String?,
        ar_id: String?,
        ar_cl_opt: String?,
        ar_cl_text: String?,
        ar_cl_att: MultipartBody.Part?,
        ar_status: String?
    ) = viewModelScope.launch {
        postActivityChecklistForm(
            token, ar_id, ar_cl_opt,
            ar_cl_text, ar_cl_att, ar_status
        )

    }

    fun setPostActivityReportFormNew(
        token: String?,
        ar_act_id: String?,
        ar_act_cat: String?,
        ar_cl_id: String?,
        ar_cl_opt: String?,
        ar_cl_text: String?,
        ar_cl_att: MultipartBody.Part?,
        ar_cl_status: String?,
    ) = viewModelScope.launch {
        postActivityReportFormNew(
            token, ar_act_id, ar_act_cat,
            ar_cl_id, ar_cl_opt, ar_cl_text, ar_cl_att, ar_cl_status
        )
    }

    private suspend fun getListActivityReport(token: String?, page: String?) {
        _listActivityReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListActivityReport(token, page)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listActivityReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listActivityReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listActivityReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listActivityReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listActivityReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getActivityChecklist(token: String?, formNum: String?) {
        _listActivityChecklist.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListActivityChecklist(token, formNum)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listActivityChecklist.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listActivityChecklist.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listActivityChecklist.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listActivityChecklist.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listActivityChecklist.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getFormGroup(token: String?) {
        _listFormGroup.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListFormGroup(token)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listFormGroup.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listFormGroup.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listFormGroup.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listFormGroup.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listFormGroup.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getFormGroupType(token: String?, form_group: String?) {
        _listFormGroupType.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListFormGroupType(token, form_group)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listFormGroupType.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listFormGroupType.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listFormGroupType.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listFormGroupType.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listFormGroupType.postValue(Event(Resource.Error(t.message!!)))
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
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
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

    private suspend fun getChecklistID(token: String?, checklistID: String?) {
        _listChecklistID.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getChecklistID(token, checklistID)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listChecklistID.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listChecklistID.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listChecklistID.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listChecklistID.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listChecklistID.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postActivityReportForm(
        token: String?,
        act_theme_id: String?,
        act_region: String?,
        act_site_id: String?,
        act_date: String?,
        act_avail: String?,
        act_notes: String?,
    ) {
        _postActivityReportForm.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postActivityReportForm(
                    token,
                    act_theme_id,
                    act_region,
                    act_site_id,
                    act_date,
                    act_avail,
                    act_notes
                )
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _postActivityReportForm.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _postActivityReportForm.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _postActivityReportForm.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _postActivityReportForm.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _postActivityReportForm.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postActivityChecklistForm(
        token: String?,
        ar_id: String?,
        ar_cl_opt: String?,
        ar_cl_text: String?,
        ar_cl_att: MultipartBody.Part?,
        ar_status: String?
    ) {
        _postActivityChecklistForm.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postActivityChecklistForm(
                    token,
                    ar_id,
                    ar_cl_opt,
                    ar_cl_text,
                    ar_cl_att,
                    ar_status
                )
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _postActivityChecklistForm.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _postActivityChecklistForm.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _postActivityChecklistForm.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _postActivityChecklistForm.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _postActivityChecklistForm.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postActivityReportFormNew(
        token: String?,
        ar_act_id: String?,
        ar_act_cat: String?,
        ar_cl_id: String?,
        ar_cl_opt: String?,
        ar_cl_text: String?,
        ar_cl_att: MultipartBody.Part?,
        ar_cl_status: String?,
    ) {
        _postActivityReportFormNew.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postActivityChecklistFormNew(
                    token, ar_act_id, ar_act_cat,
                    ar_cl_id, ar_cl_opt, ar_cl_text, ar_cl_att, ar_cl_status
                )
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _postActivityReportFormNew.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _postActivityReportFormNew.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _postActivityReportFormNew.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _postActivityReportFormNew.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _postActivityReportFormNew.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getChecklistAdditional(token: String?, formNum: String?) {
        _listChecklistAdditional.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getListAdditionalChoice(token, formNum)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.report?.message == "Token expired, please re-login." ||
                        response.body()?.report?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    _listChecklistAdditional.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listChecklistAdditional.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listChecklistAdditional.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listChecklistAdditional.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listChecklistAdditional.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}