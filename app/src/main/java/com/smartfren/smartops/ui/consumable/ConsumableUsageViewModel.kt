package com.smartfren.smartops.ui.consumable

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.ConsumableAddReportResponse
import com.smartfren.smartops.data.models.ConsumableTypeResponse
import com.smartfren.smartops.data.models.ConsumableUsageReportDetailResponse
import com.smartfren.smartops.data.models.ConsumableUsageReportResponse
import com.smartfren.smartops.ui.consumable.repository.ConsumableRepository
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
class ConsumableUsageViewModel @Inject constructor(
    application: Application,
    private val repository: ConsumableRepository,
) : AndroidViewModel(application) {
    private val _consumableUsageData =
        MutableLiveData<Event<Resource<ConsumableUsageReportResponse>>>()
    private val _consumableUsageDetailData =
        MutableLiveData<Event<Resource<ConsumableUsageReportDetailResponse>>>()
    private val _consumableUsageTypeData =
        MutableLiveData<Event<Resource<ConsumableTypeResponse>>>()
    private val _addConsumable =
        MutableLiveData<Event<Resource<ConsumableAddReportResponse>>>()

    val consumableUsageData: LiveData<Event<Resource<ConsumableUsageReportResponse>>> =
        _consumableUsageData
    val consumableUsageDetailData: LiveData<Event<Resource<ConsumableUsageReportDetailResponse>>> =
        _consumableUsageDetailData
    val consumableUsageTypeData: LiveData<Event<Resource<ConsumableTypeResponse>>> =
        _consumableUsageTypeData
    val addConsumable: LiveData<Event<Resource<ConsumableAddReportResponse>>> =
        _addConsumable

    fun setConsumableUsage(start: String?, recperpage: String?) = viewModelScope.launch {
        getConsumableUsage(start, recperpage)
    }

    fun setConsumableUsageDetail(key: String?) = viewModelScope.launch {
        getConsumableUsageDetail(key)
    }

    fun setConsumableUsageType() = viewModelScope.launch {
        getConsumableUsageType()
    }

    fun setAddConsumableUsage(
        consType: String?,
        consSiteId: String?,
        consSiteVisitDate: String?,
        consNotes: String?,
        consUserId: String?,
        consRegisterDate: String?,
        consUsageBefore: MultipartBody.Part?,
        consUsageAfter: MultipartBody.Part?
    ) = viewModelScope.launch {
        postAddConsumableUsage(
            consType,
            consSiteId,
            consSiteVisitDate,
            consNotes,
            consUserId,
            consRegisterDate,
            consUsageBefore,
            consUsageAfter
        )
    }

    private suspend fun getConsumableUsage(start: String?, recperpage: String?) {
        _consumableUsageData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getConsumableUsageReport(start, recperpage)
                if (response.isSuccessful) {
                    _consumableUsageData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _consumableUsageData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _consumableUsageData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _consumableUsageData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _consumableUsageData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getConsumableUsageDetail(key: String?) {
        _consumableUsageDetailData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getConsumableUsageDetail(key)
                if (response.isSuccessful) {
                    _consumableUsageDetailData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _consumableUsageDetailData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _consumableUsageDetailData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _consumableUsageDetailData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _consumableUsageDetailData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun getConsumableUsageType() {
        _consumableUsageTypeData.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getConsumableUsageType()
                if (response.isSuccessful) {
                    _consumableUsageTypeData.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _consumableUsageTypeData.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _consumableUsageTypeData.postValue(Event(Resource.Error("No Internet Connection.")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _consumableUsageTypeData.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _consumableUsageTypeData.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }

    private suspend fun postAddConsumableUsage(
        consType: String?,
        consSiteId: String?,
        consSiteVisitDate: String?,
        consNotes: String?,
        consUserId: String?,
        consRegisterDate: String?,
        consUsageBefore: MultipartBody.Part?,
        consUsageAfter: MultipartBody.Part?
    ) {
        _addConsumable.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.postAddConsumableUsage(
                    consType,
                    consSiteId,
                    consSiteVisitDate,
                    consNotes,
                    consUserId,
                    consRegisterDate,
                    consUsageBefore,
                    consUsageAfter
                )
                if (response.isSuccessful) {
                    _addConsumable.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _addConsumable.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _addConsumable.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _addConsumable.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _addConsumable.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}