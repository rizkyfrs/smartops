package com.smartfren.smartops.ui.webch

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.WebCHAvailabilityResponse
import com.smartfren.smartops.ui.webch.repository.WebCHAvailabilityRepository
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
class WebCHAvailabilityViewModel @Inject constructor(
    application: Application,
    private val repository: WebCHAvailabilityRepository,
) : AndroidViewModel(application) {
    private val _regionSelection = MutableLiveData<Int>()
    val regionSelection: LiveData<Int>
        get() = _regionSelection

    private val _periodSelection = MutableLiveData<Int>()
    val periodSelection: LiveData<Int>
        get() = _periodSelection

    private val _listWebCHAvailability =
        MutableLiveData<Event<Resource<WebCHAvailabilityResponse>>>()

    val webCHAvailabilityData: LiveData<Event<Resource<WebCHAvailabilityResponse>>> =
        _listWebCHAvailability

    fun listWebAvailability(token: String?, region: String?, period: String?) =
        viewModelScope.launch {
            getListWebCHAvailability(token, region, period)
        }

    fun setRegionSelected(regionSelection: Int) {
        _regionSelection.value = regionSelection
    }

    fun setRegion(): String {
        val regions = regionSelection.value
        return regions.toString()
    }

    fun setNameRegion(): String {
        var regions = ""
        when (regionSelection.value) {
            0 -> {
                regions = "All Region"
            }
            1 -> {
                regions = "JB"
            }
            2 -> {
                regions = "NS"
            }
            3 -> {
                regions = "SS"
            }
            4 -> {
                regions = "WJ"
            }
            5 -> {
                regions = "CJ"
            }
            6 -> {
                regions = "EJ"
            }
            7 -> {
                regions = "SK"
            }
        }
        return regions
    }

    fun setPeriodSelected(periodSelection: Int) {
        _periodSelection.value = periodSelection
    }

    fun setPeriod(): String {
        val periods = periodSelection.value
        return periods.toString()
    }

    fun setNamePeriod(): String {
        var periods = ""
        when (periodSelection.value) {
            1 -> {
                periods = "Daily"
            }
            2 -> {
                periods = "Weekly"
            }
            3 -> {
                periods = "Monthly"
            }
        }
        return periods
    }

    private suspend fun getListWebCHAvailability(token: String?, region: String?, period: String?) {
        _listWebCHAvailability.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getWebCHAvailability(token, region, period)
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
                    _listWebCHAvailability.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _listWebCHAvailability.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _listWebCHAvailability.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _listWebCHAvailability.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _listWebCHAvailability.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}