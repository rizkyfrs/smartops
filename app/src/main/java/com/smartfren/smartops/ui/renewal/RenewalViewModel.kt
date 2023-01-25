package com.smartfren.smartops.ui.renewal

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.smartfren.smartops.MainApp
import com.smartfren.smartops.data.models.SiteRenewalResponse
import com.smartfren.smartops.data.models.SiteStolenKRIResponse
import com.smartfren.smartops.ui.renewal.repository.RenewalRepository
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
class RenewalViewModel @Inject constructor(
    application: Application,
    private val repository: RenewalRepository,
) : AndroidViewModel(application) {
    private val _leaseDueSelection = MutableLiveData<Int>()
    val leaseDueSelection: LiveData<Int> = _leaseDueSelection

    private val _renewalReport = MutableLiveData<Event<Resource<SiteRenewalResponse>>>()
    val renewalReport: LiveData<Event<Resource<SiteRenewalResponse>>> = _renewalReport

    fun setLeaseDueSelected(leaseDueSelection: Int) {
        _leaseDueSelection.value = leaseDueSelection
    }

    fun setLeaseDue(): String {
        val leasedue = leaseDueSelection.value
        return leasedue.toString()
    }

    fun setRenewalReport(token: String?, leasedue: String?) = viewModelScope.launch {
        getRenewalReport(token, leasedue)
    }

    private suspend fun getRenewalReport(token: String?, leasedue: String?) {
        _renewalReport.postValue(Event(Resource.Loading()))
        try {
            if (hasInternetConnection<MainApp>()) {
                val response = repository.getSiteRenewal(token, leasedue)
                if (response.isSuccessful) {
                    if (response.body()?.report?.success == false) {
                        response.body()?.report?.message?.let { toast(getApplication(), it) }
                    }
                    _renewalReport.postValue(Event(Resource.Success(response.body()!!)))
                } else {
                    _renewalReport.postValue(Event(Resource.Error(response.message())))
                }
            } else {
                _renewalReport.postValue(Event(Resource.Error("No Internet Connection")))
            }
        } catch (e: HttpException) {
            when (e) {
                is IOException -> {
                    _renewalReport.postValue(Event(Resource.Error(e.message!!)))
                    toast(getApplication(), "Exception ${e.message}")
                }
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> {
                    _renewalReport.postValue(Event(Resource.Error(t.message!!)))
                    toast(getApplication(), t.message!!)
                }
            }
        }
    }
}