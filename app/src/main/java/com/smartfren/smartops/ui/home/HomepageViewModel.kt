package com.smartfren.smartops.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.dao.SiteInfoDao
import com.smartfren.smartops.dao.SiteVisitDao
import com.smartfren.smartops.data.models.CheckInResponse
import com.smartfren.smartops.data.models.FLMStatisticRepsonse
import com.smartfren.smartops.data.models.GeneralResponse
import com.smartfren.smartops.data.models.PersonalInfoResponse
import com.smartfren.smartops.database.SiteVisitRoomDatabase
import com.smartfren.smartops.entity.SiteInfo
import com.smartfren.smartops.entity.SiteVisit
import com.smartfren.smartops.ui.home.repository.HomepageRepository
import com.smartfren.smartops.util.Event
import com.smartfren.smartops.utils.Resource
import com.smartfren.smartops.utils.hasInternetConnection
import com.smartfren.smartops.utils.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class HomepageViewModel @Inject constructor(
    application: Application,
    private val repository: HomepageRepository,
) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val listSiteVisit = MutableLiveData<ArrayList<SiteVisit>>()
    private var dao: SiteVisitDao
    private var daoSiteInfo: SiteInfoDao

    private val _personalInfoData = MutableLiveData<Event<Resource<PersonalInfoResponse>>>()
    private val _flmStatisticData = MutableLiveData<Event<Resource<FLMStatisticRepsonse>>>()
    private val _updatePersonalInfo = MutableLiveData<Event<Resource<GeneralResponse>>>()
    private val _changePasswordData = MutableLiveData<Event<Resource<GeneralResponse>>>()
    private val _updateTaggingData = MutableLiveData<Event<Resource<GeneralResponse>>>()
    private val _logout = MutableLiveData<Event<Resource<GeneralResponse>>>()
    private val _checkin = MutableLiveData<Event<Resource<CheckInResponse>>>()
    private val _checkout = MutableLiveData<Event<Resource<CheckInResponse>>>()

    val personalInfoData: LiveData<Event<Resource<PersonalInfoResponse>>> = _personalInfoData
    val flmStatisticData: LiveData<Event<Resource<FLMStatisticRepsonse>>> = _flmStatisticData
    val updatePersonalInfo: LiveData<Event<Resource<GeneralResponse>>> = _updatePersonalInfo
    val changePasswordData: LiveData<Event<Resource<GeneralResponse>>> = _changePasswordData
    val updateTaggingData: LiveData<Event<Resource<GeneralResponse>>> = _updateTaggingData
    val logout: LiveData<Event<Resource<GeneralResponse>>> = _logout
    val checkin: LiveData<Event<Resource<CheckInResponse>>> = _checkin
    val checkout: LiveData<Event<Resource<CheckInResponse>>> = _checkout

    init {
        val database = SiteVisitRoomDatabase.getDatabase(context)
        dao = database.getSiteVisitDao()
        daoSiteInfo = database.getSiteInfoDao()
    }

    fun personalInfo(token: String?) = viewModelScope.launch {
        getPersonalInfo(token)
    }

    fun flmStatistic(token: String?, type: String?) = viewModelScope.launch {
        getFLMStatistic(token, type)
    }

    fun postUpdatePersonalInfo(
        token: String?,
        nik: String?,
        name: String?,
        email: String?,
        number: String?,
        pp: MultipartBody.Part?
    ) = viewModelScope.launch {
        postPersonalInfo(token, nik, name, email, number, pp)
    }

    fun changePassword(token: String?, new_password: String?) = viewModelScope.launch {
        postChangePassword(token, new_password)
    }

    fun updateTagging(token: String?, new_tag: String?) = viewModelScope.launch {
        postUpdateTagging(token, new_tag)
    }

    fun logout(token: String?) = viewModelScope.launch {
        postLogout(token)
    }

    fun checkIn(date: String?,
                reg: String?,
                site: String?,
                device_imei: String?,
                device_custodian: String?,
                lat: String?,
                long: String?) = viewModelScope.launch {
        postCheckIn(date, reg, site, device_imei, device_custodian, lat, long)
    }

    fun checkOut(checkinid: String?,
                lat: String?,
                long: String?) = viewModelScope.launch {
        postCheckOut(checkinid, lat, long)
    }

    private suspend fun getPersonalInfo(token: String?) {
        _personalInfoData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getPersonalData(token)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == true) {
                        MainApp.instance.sharedPreferences?.edit()
                            ?.putString(
                                "regionName",
                                response.body()?.getReport()?.info?.get(0)?.regionName
                            )
                            ?.putString(
                                "regionCode",
                                response.body()?.getReport()?.info?.get(0)?.regionCode
                            )
                            ?.putString(
                                "levelID",
                                response.body()?.getReport()?.info?.get(0)?.levelID
                            )
                            ?.apply()
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                        MainApp.instance.onSessionExpired(getApplication())
                    } else {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    }
                    _personalInfoData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    if (response.code() == 405) {
                        _personalInfoData.postValue(Event(Resource.Error("Token invalid, please re-login.")))
                        MainApp.instance.onSessionExpired(getApplication())
                    } else {
                        _personalInfoData.postValue(Event(Resource.Error(response.message())))
                    }
                }
            } else {
                _personalInfoData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _personalInfoData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _personalInfoData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getFLMStatistic(token: String?, type: String?) {
        _flmStatisticData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getFLMStatisticData(token, type)
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
                    _flmStatisticData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _flmStatisticData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _flmStatisticData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _flmStatisticData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _flmStatisticData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postPersonalInfo(
        token: String?,
        nik: String?,
        name: String?,
        email: String?,
        number: String?,
        pp: MultipartBody.Part?
    ) {
        _updatePersonalInfo.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postPersonalInfo(token, nik, name, email, number, pp)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    _updatePersonalInfo.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _updatePersonalInfo.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _updatePersonalInfo.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _updatePersonalInfo.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _updatePersonalInfo.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postLogout(token: String?) {
        _logout.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postLogout(token)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                    }
//                    removeFirebaseMessage()
                    response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    _logout.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _logout.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _logout.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _logout.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _logout.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postChangePassword(token: String?, new_password: String?) {
        _changePasswordData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postChangePassword(token, new_password)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    _changePasswordData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _changePasswordData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _changePasswordData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _logout.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _logout.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postUpdateTagging(token: String?, new_tag: String?) {
        _updateTaggingData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postUpdateTagging(token, new_tag)
                if (response.isSuccessful) {
                    if (response.body()?.getReport()?.success == false) {
                        response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    } else if (response.body()
                            ?.getReport()?.message == "Token expired, please re-login." ||
                        response.body()?.getReport()?.message == "Token invalid, please re-login."
                    ) {
                        MainApp.instance.onSessionExpired(getApplication())
                    }
                    response.body()?.getReport()?.message?.let { toast(getApplication(), it) }
                    _updateTaggingData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _updateTaggingData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _updateTaggingData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _logout.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _logout.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postCheckIn(
        date: String?,
        reg: String?,
        site: String?,
        device_imei: String?,
        device_custodian: String?,
        lat: String?,
        long: String?
    ) {
        _checkin.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postCheckIn(date, reg, site, device_imei, device_custodian, lat, long)
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _checkin.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _checkin.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _checkin.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _checkin.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _checkin.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postCheckOut(
        checkinid: String?,
        lat: String?,
        long: String?
    ) {
        _checkout.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postCheckOut(checkinid, lat, long)
                if (response.isSuccessful) {
                    if (response.body()?.success == false) {
                        response.body()?.failureMessage?.let { toast(getApplication(), it) }
                    }
                    _checkout.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _checkout.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _checkout.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _checkout.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _checkout.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    fun insertNote(sitevisit: SiteVisit) {
        dao.insert(sitevisit)
    }

    fun insertSiteIfo(siteinfo: SiteInfo) {
        daoSiteInfo.insert(siteinfo)
    }

    fun getSiteVisit(): LiveData<ArrayList<SiteVisit>> {
        return listSiteVisit
    }

    private fun removeFirebaseMessage() {
//        CoroutineScope(Dispatchers.Default).launch {
            FirebaseMessaging.getInstance().isAutoInitEnabled = false
            FirebaseInstallations.getInstance().delete()
            FirebaseMessaging.getInstance().deleteToken()
//        }
    }
}